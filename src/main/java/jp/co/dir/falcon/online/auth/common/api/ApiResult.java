package jp.co.dir.falcon.online.auth.common.api;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    //ステータスコード
    private int code;

    //戻りデータ
    private T data;

    //結果情報
    private String message;

    //時間文字列
    private String time;

    private ApiResult() {

    }

    //成功コンストラクターを定義する
    private ApiResult(T data) {
        this.code = ApiCode.SUCCESS.getCode();
        this.message = ApiCode.SUCCESS.getMsg();
        this.data = data;
        this.time = LocalDateTime.now().toString();

    }

    private ApiResult(ApiCode apiCode) {
        this.code = apiCode.getCode();
        this.message = apiCode.getMsg();
        this.time = LocalDateTime.now().toString();
    }

    private ApiResult(int code, String msg) {
        this.code = code;
        this.message = msg;
        this.time = LocalDateTime.now().toString();
    }

    private ApiResult(ApiCode apiCode, T data) {
        this.code = apiCode.getCode();
        this.message = apiCode.getMsg();
        this.data = data;
        this.time = LocalDateTime.now().toString();
    }

    /**
     * 成功すると呼び出される
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult(data);
    }

    /**
     * ステータスに基づいて結果を返す
     *
     * @param apiCode
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> build(ApiCode apiCode) {
        return new ApiResult(apiCode);
    }

    /**
     * コードとメッセージに基づいて結果を返します
     *
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> build(int code, String msg) {
        return new ApiResult(code, msg);
    }

    /**
     * ステータスとデータに基づいて結果を返す
     *
     * @param apiCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> build(ApiCode apiCode, T data) {
        return new ApiResult(apiCode, data);
    }


    /**
     * 異常な結果を返す
     *
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> error(int code, String msg) {
        return new ApiResult(code, msg);
    }
}




