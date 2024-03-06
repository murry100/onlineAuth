package jp.co.dir.falcon.online.auth.common.exception;

import jp.co.dir.falcon.online.auth.common.api.ApiCode;
import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //自定义异常
    @ExceptionHandler(BusinessException.class)
    public ApiResult systemExceptionHandler(BusinessException e) {
        log.error("BusinessException全局异常：{}",e);
        return ApiResult.error(e.getCode(), e.getMsg());
    }

    //系统异常
    @ExceptionHandler(Exception.class)
    public ApiResult exceptionHandler(Exception e) {
        log.error("Exception全局异常：{}",e);
        return ApiResult.error(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage());
    }


}




