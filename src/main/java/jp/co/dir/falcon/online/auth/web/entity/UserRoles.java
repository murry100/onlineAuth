package jp.co.dir.falcon.online.auth.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)//チェーン; アクセサー。 このアノテーションは、ゲッター メソッドとセッター メソッドの形式を制御できます。
@TableName("user_roles")
public class UserRoles {
    private Integer userId;

    private Integer roleId;

    private Timestamp createdDatetime;

    private String createdBy;
}
