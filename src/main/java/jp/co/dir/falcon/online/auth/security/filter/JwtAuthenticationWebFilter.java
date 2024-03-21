package jp.co.dir.falcon.online.auth.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import jp.co.dir.falcon.online.auth.common.utils.JwtUtils;
import jp.co.dir.falcon.online.auth.common.utils.RedisUtil;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String path = exchange.getRequest().getPath().toString();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        if (pathMatcher.match("/auth/ver1/login/authenticate", path)) {
            return chain.filter(exchange);
        }

        if (StringUtils.isEmpty(authorization)) {
            // authorizationにはローが存在せず、直接returnが返される
            throw new RuntimeException("資格認定不合格");
        }

        String token = authorization.substring(7);

        // 解析トークン
        String userId = null;

        LogUser loginUser = new LogUser();

        DecodedJWT tokenInfo = JwtUtils.verifyToken(token);

        //トーケン有効期限
        Date expiresAt = tokenInfo.getExpiresAt();
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (pathMatcher.match("/auth/ver1/login/mfa", path)) {
            userId = tokenInfo.getClaim("user_id").asString();

            // useridを取得redisからユーザー情報を取得する
            if (userId == null || userId.equals("")) {
                throw new RuntimeException("ユーザーがログインしていません");
            }
        } else {
            Gson gson = new Gson();
            loginUser = gson.fromJson(tokenInfo.getClaim("logUser").asString(), LogUser.class);
            if (Objects.isNull(loginUser)) {
                throw new RuntimeException("ユーザーがログインしていません");
            }

            Map<String, Set> roleMap = (Map<String, Set>) redisUtil.get("Authorization");
            Boolean Authorization = loginUser.getPermissions().stream().anyMatch(role -> {
                return roleMap.containsKey(role) && roleMap.get(role).stream().anyMatch(url -> {
                    return pathMatcher.match(path, (String) url);
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

        // ゆるめる
        return chain.filter(exchange);
    }
}