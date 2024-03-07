package jp.co.dir.falcon.online.auth.security.config;

import jp.co.dir.falcon.online.auth.security.filter.JwtAuthenticationFilter;
import jp.co.dir.falcon.online.auth.security.service.AccessDeniedHandlerImpl;
import jp.co.dir.falcon.online.auth.security.service.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@EnableWebSecurity //因为我引入了spring-boot-starter-security，所以不用@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
public class SecurityConfig{
    private static final String[] AUTH_WHITELIST = {
            "/sysUser/login",
            "/sysUser/test",
            "/test/**",
            "/**.html",
            "/js/**",
            "/css/**",
            "/img/**"
    };


    //将authenticationManager注入容器中，再自定义登录接口中获取进行认证
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration autheConfiguration) throws Exception {
        return autheConfiguration.getAuthenticationManager();
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;



    //注入加密方式--后面就会使用这种方式进行对密码的对比（明文与密码的对比是否匹配）
    // 而不使用默认的密码验证
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //配置放行的规则
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable)// 关闭csrf验证(防止跨站请求伪造攻击)由于我们的资源都会收到SpringSecurity的保护，所以想要跨域访问还要让SpringSecurity运行跨域访问
                // 不通过session 获取SecurityContext(基于Token不需要session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //开启权限拦截
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                // 认证授权异常自定义处理
                .exceptionHandling(m -> {
                    m.authenticationEntryPoint(authenticationEntryPoint);//自定义认证失败异常处理类
                    m.accessDeniedHandler(accessDeniedHandler);//自定义授权失败异常处理类
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                ;

        // 禁用缓存
//        http.headers().cacheControl();

        // 跨域请求配置
        http.cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

}


