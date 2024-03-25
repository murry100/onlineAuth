package jp.co.dir.falcon.online.auth.common.api;

import lombok.Data;

@Data
public class ApiMsg {
    private final String FAILURE_TO_QUALIFY = "資格認定不合格";
    private final String NOT_AUTH = "認証なし";
}
