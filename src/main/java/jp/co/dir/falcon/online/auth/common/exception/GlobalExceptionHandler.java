package jp.co.dir.falcon.online.auth.common.exception;

import jp.co.dir.falcon.online.auth.common.api.ApiCode;
import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * グローバル例外処理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //カスタム例外
    @ExceptionHandler(BusinessException.class)
    public ApiResult systemExceptionHandler(BusinessException e) {
        log.error("BusinessExceptionグローバル例外：{}", e);
        return ApiResult.error(e.getCode(), e.getMsg());
    }

    //システム例外
    @ExceptionHandler(Exception.class)
    public ApiResult exceptionHandler(Exception e) {
        log.error("Exceptionグローバル例外：{}", e);
        return ApiResult.error(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage());
    }


}




