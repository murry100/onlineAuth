package jp.co.dir.falcon.online.auth.web.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jp.co.dir.falcon.online.auth.common.api.ApiCode;
import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.common.utils.JwtUtils;
import jp.co.dir.falcon.online.auth.common.utils.RedisUtil;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.entity.RolePermissions;
import jp.co.dir.falcon.online.auth.web.entity.Users;
import jp.co.dir.falcon.online.auth.web.mapper.SysPermissionMapper;
import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
import jp.co.dir.falcon.online.auth.web.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysUserMapper userMapper;

    private static Long tokenAmount;

    private static Long otpAmount;

    @Value("${spring.data.redis.token-validity-in-seconds}")
    public void tokenAmount(Long tokenAmount) {
        OtpServiceImpl.tokenAmount = tokenAmount;
    }

    @Value("${spring.data.redis.otp-validity-in-seconds}")
    public void otpAmount(Long otpAmount) {
        OtpServiceImpl.otpAmount = otpAmount;
    }

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOTP(String phoneNumber) {
        if(redisUtil.hasKey(phoneNumber)){
            return "";
        }
        String otp = String.valueOf(100000 + secureRandom.nextInt(900000)); // 6桁のOTP世代
        redisUtil.set(phoneNumber, otp, 60); // Redisは1つのパーティションに保存します
        sendOtpViaSms(phoneNumber, otp); // SMS OTP 配信
        return otp;
    }

    private void sendOtpViaSms(String phoneNumber, String otp) {
        // SMS配信装置。 サービスプロバイダーのAPIを利用する
    }

    public ApiResult validateOtp(String inputOtp, ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = authorization.substring(7);
        DecodedJWT tokenInfo = JwtUtils.verifyToken(token);
        String userId = tokenInfo.getClaim("user_id").asString();
        //ユーザー名に基づいてユーザーをクエリする
        Users user = userMapper.selectOne(new QueryWrapper<Users>().eq("id", Integer.parseInt(userId)));
        if (user == null) {
            throw new RuntimeException("ユーザーは存在しません");
        }
        String storedOtp = (String) redisUtil.get(user.getPhoneNumber());
        if (storedOtp != null && storedOtp.equals(inputOtp)) {
            Set<String> permissionsSet = new HashSet<>();
            List<RolePermissions> sysPermissions = new ArrayList<>();

            if (user != null) {
                //このユーザーが所有する権限を取得します
                sysPermissions = sysPermissionMapper.selectPermissionList(user.getUserId());

                // ユーザー権限の宣言
                sysPermissions.forEach(sysPermission -> {
                    permissionsSet.add(sysPermission.getRoleId().toString());
                });
            }

            LogUser logUser = new LogUser(user, permissionsSet.stream().toList());

            Map<String, String> payloadMap = new HashMap<>();
            Gson gson = new Gson();
            payloadMap.put("logUser", gson.toJson(logUser));
            payloadMap.put("sub", logUser.getUsername());
            payloadMap.put("auth", logUser.getPermissions().stream().reduce("", (a, b) -> a.isEmpty() ? b : a + " " + b));
            String jwt = JwtUtils.generateToken(payloadMap);

            Map<String, Object> payloadResMap = new HashMap<>();
            redisUtil.set(jwt, logUser, tokenAmount); // RedisにJWTを20分間保存
            payloadResMap.put("id_token", jwt);

            Date updateDate = user.getPasswordUpdatedDatetime();
            String repFlg = PasswordExpirationCheck(updateDate.toString(), otpAmount);
            payloadResMap.put("repFlg", repFlg);
            payloadResMap.put("permissions", sysPermissions);

            return ApiResult.success(payloadResMap);
        }
        return ApiResult.error(ApiCode.SYSTEM_ERROR.getCode(), "認証コードの有効期限またはエラー");
    }

    private String PasswordExpirationCheck(String lastUpdated, long passwordValidityDays) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'JST' yyyy");

        LocalDateTime lastUpdatedDateTime = LocalDateTime.parse(lastUpdated, formatter);

        // 计算当前时间与最后更新时间的差值
        long daysBetween = ChronoUnit.DAYS.between(lastUpdatedDateTime, LocalDateTime.now());

        // 检查密码是否过期
        return daysBetween > passwordValidityDays ? "1" : "0";
    }
}