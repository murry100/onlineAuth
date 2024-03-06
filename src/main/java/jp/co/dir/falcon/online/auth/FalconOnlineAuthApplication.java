package jp.co.dir.falcon.online.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("jp.co.dir.falcon.online.auth.web.mapper")
public class FalconOnlineAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FalconOnlineAuthApplication.class, args);
    }

}
