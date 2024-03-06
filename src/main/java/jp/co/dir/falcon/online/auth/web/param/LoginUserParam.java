package jp.co.dir.falcon.online.auth.web.param;

import lombok.Data;

@Data
public class LoginUserParam {

    //用户名
    private String userName;

    //用户密码
    private String password;
}
