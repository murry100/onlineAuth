package jp.co.dir.falcon.online.auth.web.controller;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.web.param.LoginUserParam;
import jp.co.dir.falcon.online.auth.web.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@RequestMapping("/auth")
public class SysUserController {

    @Autowired
    private LogService logService;


    /**
     * カスタムログイン
     *
     * @param param ログインしてパラメータを渡す
     * @return
     */
    @PostMapping("/ver1/login/authenticate")
    public ApiResult login(@RequestBody LoginUserParam param) {

        return logService.login(param);

    }


    /**
     * カスタムログアウト
     *
     * @return
     */
    @PostMapping("/ver1/logOut")
    public ApiResult logOut(ServerWebExchange exchange) {

        return logService.logOut(exchange);

    }


}

