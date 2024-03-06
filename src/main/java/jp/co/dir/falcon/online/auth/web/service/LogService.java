package jp.co.dir.falcon.online.auth.web.service;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.web.param.LoginUserParam;

public interface LogService {

    ApiResult login(LoginUserParam param);

    ApiResult logOut();
}

