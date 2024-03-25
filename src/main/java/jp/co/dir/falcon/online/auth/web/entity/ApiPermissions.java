package jp.co.dir.falcon.online.auth.web.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiPermissions implements Serializable {
    private Integer roleId;

    private String method;

    private String endpoint;
}
