package jp.co.dir.falcon.online.auth.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import jp.co.dir.falcon.online.auth.common.api.ApiCode;
import jp.co.dir.falcon.online.auth.common.api.ApiMsg;
import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.common.utils.JwtUtils;
import jp.co.dir.falcon.online.auth.common.utils.RedisUtil;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.entity.ApiPermissions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBufferUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class JwtAuthenticationWebFilter implements WebFilter {

    private final RedisUtil redisUtil;

    @Autowired
    public JwtAuthenticationWebFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String path = exchange.getRequest().getPath().toString();
            String method = exchange.getRequest().getMethod().toString();
            AntPathMatcher pathMatcher = new AntPathMatcher();

            if (pathMatcher.match("/auth/ver1/login/authenticate", path) || pathMatcher.match("/auth/ver1/logOut", path)) {
                return chain.filter(exchange);
            }

            String token = authorization.substring(7);

            // 解析トークン
            String userId = null;

            LogUser loginUser = new LogUser();

            DecodedJWT tokenInfo = JwtUtils.verifyToken(token);

            //トーケン有効期限
            Date expiresAt = tokenInfo.getExpiresAt();
            SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (pathMatcher.match("/auth/ver1/login/mfa", path) || pathMatcher.match("/auth/ver1/otp/send", path)) {
                userId = tokenInfo.getClaim("user_id").asString();


                // useridを取得redisからユーザー情報を取得する
                if (userId == null || userId.equals("")) {
                    return apiResult(exchange, HttpStatus.UNAUTHORIZED.value(), "ユーザーがログインしていません");
                }
            } else {
                loginUser = (LogUser) redisUtil.get(token);
                if (Objects.isNull(loginUser)) {
                    throw new RuntimeException("ユーザーがログインしていません");
                }

                Map<Integer, List<ApiPermissions>> roleMap = (Map<Integer, List<ApiPermissions>>) redisUtil.get("Authorization");
                Boolean Authorization = loginUser.getPermissions().stream().anyMatch(role -> {
                    Integer roleId = Integer.parseInt(role);
                    return roleMap.containsKey(roleId) && roleMap.get(roleId).stream().anyMatch(permissions -> {
                        return pathMatcher.match(path, permissions.getEndpoint()) && method.equals(permissions.getMethod());
                    });
                });

                if (Authorization) {
                    //SecurityContextHolderへのユーザー情報の格納
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new RuntimeException("ユーザーが権限がありません");
                }
            }
        }catch (VerifyError e){
            return apiResult(exchange, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }catch (Throwable e){
            return apiResult(exchange, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        // ゆるめる
        return chain.filter(exchange);
    }

    private Mono<Void> apiResult (ServerWebExchange exchange, Integer code, String msg) {
        ApiResult<?> apiResult = ApiResult.error(code, msg);

        String json = new Gson().toJson(apiResult);

        DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}