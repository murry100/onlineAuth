package jp.co.dir.falcon.online.auth.web.param;

import lombok.Data;

@Data
public class LoginUserParam {

    //ユーザー名
    private String userName;

    //ユーザーのパスワード
    private String password;
}
