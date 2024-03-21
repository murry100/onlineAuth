package jp.co.dir.falcon.online.auth.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/testPreAuthorize")
public class TestPreAuthorizeController {


    @PostMapping("/hello")
    // 只有sys:queryUser 权限才能访问
    //@PreAuthorize("hasAuthority('sys:queryUser')") //这是没有自定义权限校验方法的默认写法
//    @PreAuthorize("@syex.hasAuthority('admin')")
    public Mono<String> hello() {

        return Mono.just("hello");
    }

    @PostMapping("/hello2")
    // 只有sys:queryUser2 权限才能访问
//    @PreAuthorize("@syex.hasAuthority('test')")
    public Mono<String> hello2() {

        return Mono.just("hello2");
    }
}

