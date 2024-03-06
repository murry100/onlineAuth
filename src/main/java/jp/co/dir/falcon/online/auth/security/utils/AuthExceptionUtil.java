package jp.co.dir.falcon.online.auth.security.utils;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfException;

//认证异常工具类
public class AuthExceptionUtil {

    public static ApiResult getErrMsgByExceptionType(AuthenticationException e) {

        if (e instanceof LockedException) {

            return ApiResult.error(1100,"账户被锁定，请联系管理员!");

        } else if (e instanceof CredentialsExpiredException) {
            return ApiResult.error(1105,"用户名或者密码输入错误!");

        }else if (e instanceof InsufficientAuthenticationException) {
            return ApiResult.error(403,"请登录!");

        } else if (e instanceof AccountExpiredException) {
            return ApiResult.error(1101, "账户过期，请联系管理员!");

        } else if (e instanceof DisabledException) {
            return ApiResult.error(1102, ("账户被禁用，请联系管理员!"));

        } else if (e instanceof BadCredentialsException) {
            return ApiResult.error(1105, "用户名或者密码输入错误!");

        }else if (e instanceof AuthenticationServiceException) {

            return ApiResult.error(1106, "认证失败，请重试!");
        }

        return ApiResult.error(1200, e.getMessage());
    }

    public static ApiResult getErrMsgByExceptionType(AccessDeniedException e) {

        if (e instanceof CsrfException) {

            return ApiResult.error(-1001, "非法访问跨域请求异常!");
        } else if (e instanceof CsrfException) {

            return ApiResult.error(-1002,"非法访问跨域请求异常!");
        } else if (e instanceof AuthorizationServiceException) {

            return ApiResult.error(1101, "认证服务异常请重试!");
        }else if (e instanceof AccessDeniedException) {

            return ApiResult.error(4003, "权限不足不允许访问!");
        }

        return ApiResult.error(1200, e.getMessage());
    }

}



