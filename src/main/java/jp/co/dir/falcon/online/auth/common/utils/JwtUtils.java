package jp.co.dir.falcon.online.auth.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtils {
    private static String secretKey;

    private static Integer amount;

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    public void amount(Integer amount) {
        JwtUtils.amount = amount;
    }

    @Value("${security.authentication.jwt.base64-secret}")
    public void secretKey(String secretKey) {
        JwtUtils.secretKey = secretKey;
    }


    /**
     * 作成token
     *
     * @param payloadMap 保存されたコンテンツ、カスタマイズされた、通常はユーザー ID
     * @return
     */
    public static String generateToken(Map<String, String> payloadMap) {

        HashMap headers = new HashMap();

        JWTCreator.Builder builder = JWT.create();

        //jwtの有効期限を定義する
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, amount);


        //payload
        payloadMap.forEach((k, v) -> {
            builder.withClaim(k, v);
        });


        // 生成token
        String token = builder.withHeader(headers)//header
                .withClaim("second", amount)//jwt の有効期限/秒を使用すると、jwt の有効期限が近づいたときに自動的に更新できます。
                .withExpiresAt(instance.getTime())//トークンの有効期限を指定する
                .sign(Algorithm.HMAC256(secretKey));//サイン


        return token;
    }


    /**
     * トークンが合法かどうかを確認する
     *
     * @param token
     * @return
     */
    public static DecodedJWT verifyToken(String token) {

        /*
        検証例外がある場合は、ここで例外がスローされます。
        SignatureVerificationException 署名の不一致の例外
        TokenExpiredException トークンの有効期限の例外
        AlgorithmMismatchException アルゴリズム不一致例外
        InvalidClaimException 無効なペイロードの例外
        */
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);


        return decodedJWT;
    }

    /**
     * トークン情報の取得
     *
     * @param token
     * @return
     */
    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        return decodedJWT;
    }
}



