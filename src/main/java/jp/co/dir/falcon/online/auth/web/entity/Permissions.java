package jp.co.dir.falcon.online.auth.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)//チェーン; アクセサー。 このアノテーションは、ゲッター メソッドとセッター メソッドの形式を制御できます。
@TableName("permissions")
public class Permissions implements Serializable {
    @TableId(value = "function_id", type = IdType.ASSIGN_ID)
    private String functionId;

    private String permissionKbn;

    private String permissionName;

    private String method;

    private String endpoint;
}
