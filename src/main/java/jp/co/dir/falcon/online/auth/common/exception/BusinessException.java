package jp.co.dir.falcon.online.auth.common.exception;

import jp.co.dir.falcon.online.auth.common.api.ApiCode;
import lombok.Data;


/**
 * 自定义异常类
 */
@Data
public class BusinessException extends RuntimeException{

    private int code;

    private String msg;


    public BusinessException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.code = apiCode.getCode();
        this.msg = apiCode.getMsg();
    }


}




