package jp.co.dir.falcon.online.auth.security.utils;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfException;

//認証例外ツールクラス
public class AuthExceptionUtil {

    public static ApiResult getErrMsgByExceptionType(AuthenticationException e) {

        if (e instanceof LockedException) {

            return ApiResult.error(1100, "アカウントがロックされています。管理者に連絡してください!");

        } else if (e instanceof CredentialsExpiredException) {
            return ApiResult.error(1105, "ユーザー名またはパスワードが間違って入力されました!");

        } else if (e instanceof InsufficientAuthenticationException) {
            return ApiResult.error(403, "サインインしてください!");

        } else if (e instanceof AccountExpiredException) {
            return ApiResult.error(1101, "アカウントの有効期限が切れています。管理者に連絡してください!");

        } else if (e instanceof DisabledException) {
            return ApiResult.error(1102, ("アカウントが無効になっています。管理者に連絡してください!"));

        } else if (e instanceof BadCredentialsException) {
            return ApiResult.error(1105, "ユーザー名またはパスワードが間違って入力されました!");

        } else if (e instanceof AuthenticationServiceException) {

            return ApiResult.error(1106, "認証に失敗しました。もう一度やり直してください!");
        }

        return ApiResult.error(1200, e.getMessage());
    }

    public static ApiResult getErrMsgByExceptionType(AccessDeniedException e) {

        if (e instanceof CsrfException) {

            return ApiResult.error(-1001, "クロスドメインリクエスト例外への不正アクセス!");
        } else if (e instanceof CsrfException) {

            return ApiResult.error(-1002, "クロスドメインリクエスト例外への不正アクセス!");
        } else if (e instanceof AuthorizationServiceException) {

            return ApiResult.error(1101, "認証サービス例外。もう一度お試しください。!");
        } else if (e instanceof AccessDeniedException) {

            return ApiResult.error(4003, "権限が不十分なためアクセスが許可されません!");
        }

        return ApiResult.error(1200, e.getMessage());
    }

}



