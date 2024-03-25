package jp.co.dir.falcon.online.auth.web.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.common.utils.JwtUtils;
import jp.co.dir.falcon.online.auth.web.entity.Users;
import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
import jp.co.dir.falcon.online.auth.web.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

@RestController
@RequestMapping("/auth")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @Autowired
    private SysUserMapper userMapper;

    @PostMapping("/ver1/otp/send")
    public String sendOtp(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = authorization.substring(7);
        DecodedJWT tokenInfo = JwtUtils.verifyToken(token);
        String userId = tokenInfo.getClaim("user_id").asString();
        //ユーザー名に基づいてユーザーをクエリする
        Users user = userMapper.selectOne(new QueryWrapper<Users>().eq("id", Integer.parseInt(userId)));
        if (user == null) {
            throw new RuntimeException("ユーザーは存在しません");
        }
        String phoneNumber = user.getPhoneNumber();
        return otpService.generateOTP(phoneNumber);
    }

    @GetMapping("/ver1/login/mfa")
    public ApiResult validateOtp(@RequestParam String otp, ServerWebExchange exchange) {
        return otpService.validateOtp(otp, exchange);
    }
}
