package jp.co.dir.falcon.online.auth.security.config;

import jp.co.dir.falcon.online.auth.common.utils.RedisUtil;
import jp.co.dir.falcon.online.auth.security.filter.JwtAuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    private static final String[] AUTH_WHITELIST = {
            "/auth/ver1/login/authenticate",
            "/auth/ver1/logOut",
            "/auth/ver1/login/mfa",
            "/testPreAuthorize/**",
            "/test/**",
            "/**.html",
            "/js/**",
            "/css/**",
            "/img/**"
    };

    @Autowired
    private RedisUtil redisUtil;

    @Bean
    @Lazy
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //注入暗号化方式：後にこの方式を使用してパスワードの比較を行います（平文とパスワードの比較が一致するかどうか）
    //デフォルトのパスワード認証を使用せずに
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //ローを設定する規則
    @Bean
    public SecurityWebFilterChain SecurityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable())// csrf認証をオフにする（クロスステーション要求による偽造攻撃を防ぐ）私たちのリソースはすべてSpringSecurityによって保護されるため、クロスドメインアクセスを実行するにはSpringSecurityにクロスドメインアクセスを実行させる必要があります
                //开启权限拦截
                .authorizeExchange( auth -> auth
                        .pathMatchers(AUTH_WHITELIST).permitAll()
//                        .pathMatchers("/sysUser/logOut").hasAuthority("admin")
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtAuthenticationWebFilter(redisUtil), SecurityWebFiltersOrder.AUTHENTICATION)
                .cors(withDefaults())
                .httpBasic(withDefaults())
        ;

        return http.build();
    }

}
