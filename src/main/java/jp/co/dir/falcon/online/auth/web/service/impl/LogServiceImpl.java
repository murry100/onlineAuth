package jp.co.dir.falcon.online.auth.web.service.impl;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.common.utils.JwtUtils;
import jp.co.dir.falcon.online.auth.common.utils.RedisUtil;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.param.LoginUserParam;
import jp.co.dir.falcon.online.auth.web.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.*;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static Long tokenAmount;

    @Value("${spring.data.redis.token-validity-in-seconds}")
    public void tokenAmount(Long tokenAmount) {
        LogServiceImpl.tokenAmount = tokenAmount;
    }

    @Override
    public ApiResult login(LoginUserParam param) {

        // 1 AuthenticationManager オブジェクトを取得し、authenticate() メソッドを呼び出します。
        // UsernamePasswordAuthenticationToken は認証インターフェイスを実装します。
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(param.getUserName(), param.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //2 認証に失敗しました 認証に失敗しました。
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("認証に失敗しました。ユーザー情報が存在しません。");
        }


        //認証に合格しました。ユーザー ID を使用して jwt トークンを生成します。
        LogUser loginUser = (LogUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getUserId().toString();

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("user_id", userId);
        String jwt = JwtUtils.generateToken(payloadMap);
        payloadMap.clear();

        payloadMap.put("id_token", jwt);

        //roleKey -> url
        Map<String, Set> roleMap = new HashMap<>();
        Set<String> adminSet = new HashSet<>();
        adminSet.add("/auth/ver1/login/authenticate");
        adminSet.add("/auth/ver1/logOut");
        adminSet.add("/auth/ver1/login/mfa");
        adminSet.add("/testPreAuthorize/hello");
        roleMap.put("admin", adminSet);
        redisUtil.set("Authorization", roleMap);

        return ApiResult.success(payloadMap);
    }

    @Override
    public ApiResult logOut(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = authorization.substring(7);
        redisUtil.del(token);
        return ApiResult.success("正常に終了します!");

    }
}


