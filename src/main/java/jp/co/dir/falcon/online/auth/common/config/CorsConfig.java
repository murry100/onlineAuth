package jp.co.dir.falcon.online.auth.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//クロスリクエスト構成クラス
@Configuration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //  クロスドメイン アドレスが必要なので、ここに注意してください。 127.0.0.1 != localhost
        // * すべてのアドレスにアクセスできることを示します
        corsConfiguration.addAllowedOrigin("*");  // 1
        //  クロスドメインリクエストヘッダー
        corsConfiguration.addAllowedHeader("*"); // 2
        //  クロスドメインリクエスト方式
        corsConfiguration.addAllowedMethod("*"); // 3
        //この文を大まかに追加すると、持ち運びできるという意味になります cookie
        //最終的な結果は、クロスドメインリクエスト中に同じものを取得できることです。 session
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //アクセス可能なアドレスを構成する
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}

