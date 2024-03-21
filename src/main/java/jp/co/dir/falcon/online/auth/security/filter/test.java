//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.google.common.collect.Maps;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import org.apache.commons.compress.utils.Lists;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Configuration
//public class SecurityBasicAuthorizationFilter implements GlobalFilter, Ordered {
//
//    private static final String UNAUTHORIZED_MESSAGE = "unauthorized";
//
//    @Value("${jwt.secret}")
//    String secret;
//    @Value("${access.topic.logs}")
//    String accessLogsTopic;
//
//    @Autowired
//    WhiteListProperties whiteListUri;
//
//    private JwtParser jwtParser;
//
//    private synchronized JwtParser getParser() {
//        if (jwtParser == null) {
//            jwtParser = Jwts.parser().setSigningKey(secret);
//        }
//        return jwtParser;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        List<String> urls = whiteListUri.getWhiteList();
//        String requestUrl = request.getPath().value();
//        for (String url : urls) {
//            if (pathMatcher.match(url, requestUrl)) {
//                return chain.filter(exchange);
//            }
//        }
//        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        ServerHttpResponse response = exchange.getResponse();
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            return this.error(response, HttpStatus.FORBIDDEN.value(), UNAUTHORIZED_MESSAGE);
//        }
//        String token = authorization.substring(7);
//        if (CharSequenceUtil.isBlank(token)) {
//            return this.error(response, HttpStatus.FORBIDDEN.value(), UNAUTHORIZED_MESSAGE);
//        }
//        String openid = "";
//        try {
//            Claims claims = getParser().parseClaimsJws(token).getBody();
//            openid = claims.get("login_user", String.class);
//            if (CharSequenceUtil.isBlank(openid)) {
//                return this.error(response, HttpStatus.FORBIDDEN.value(), UNAUTHORIZED_MESSAGE);
//            }
//        } catch (Exception e) {
//            return this.error(response, HttpStatus.FORBIDDEN.value(), UNAUTHORIZED_MESSAGE);
//        }
//
//        String userRole = (String) RedisHelper.getRedis().opsForValue().get(openid);
//        if (CharSequenceUtil.isBlank(userRole)) {
//            return this.error(response, HttpStatus.FORBIDDEN.value(), UNAUTHORIZED_MESSAGE);
//        }
//
//        JSONObject jsonObject = JSONUtil.parseObj(userRole);
//        String projectId = jsonObject.getStr("PROJECT_ID");
//        String userId = jsonObject.getStr("USER_ID");
//        long roleId = jsonObject.getLong("ROLE_ID");
//        ServerHttpRequest req = request.mutate()
//                .header("userOpenId", openid).header("userId", userId).header("projectId", projectId)
//                .build();
//        if ("0".equals(userId)) {
//            return chain.filter(exchange.mutate().request(req).build());
//        }
//        List<String> retAuth  = Lists.newArrayList();
//        ArrayList<String> roles = (ArrayList<String>) RedisHelper.getRedis().opsForValue().get("ROLE_INFO_" + roleId);
//        if (roles != null && CollUtil.isNotEmpty(roles)){
//            retAuth  = roles.stream().filter(x -> request.getPath().value().startsWith(x)).collect(Collectors.toList());
//        }
//        if (CollUtil.isNotEmpty(retAuth)) {
//            sendMessage(userId, projectId, requestUrl);
//            return chain.filter(exchange.mutate().request(req).build());
//        } else {
//            return this.error(response, HttpStatus.FORBIDDEN.value(), UNAUTHORIZED_MESSAGE);
//        }
//
//
//    }
//
//    private void sendMessage(String userId, String projectId, String requestUrl) {
//        try {
//            LogVo logVo = LogVo.builder()
//                    .userId(userId)
//                    .projectId(projectId)
//                    .requestUrl(requestUrl)
//                    .actionTime(System.currentTimeMillis())
//                    .build();
//            KafkaHelper.sendMessage(accessLogsTopic, logVo);
//        } catch (Exception e) {
//            // dl
//        }
//    }
//
//    private Mono<Void> error(ServerHttpResponse response, int code, String message) {
//        Map<String, Object> result = Maps.newHashMap();
//        result.put("status", code);
//        result.put("code", code);
//        result.put("message", message);
//        result.put("success", false);
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonHelper.obj2json(result).getBytes(StandardCharsets.UTF_8))));
//    }
//
//    @Override
//    public int getOrder() {
//        return 10;
//    }
//}
