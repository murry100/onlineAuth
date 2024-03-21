package jp.co.dir.falcon.online.auth.common.api;

public enum ApiCode {

    SUCCESS(200, "成功"),

    SYSTEM_ERROR(500, "操作に失敗しました"),

    NOT_FOUND(404, "リソースが見つかりませんでした");

    private final int code;
    private final String msg;

    ApiCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}


