package jp.co.dir.falcon.online.auth.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)//链式; 存取器。通过该注解可以控制getter和setter方法的形式。
@TableName("sys_user_permission_relation")
public class SysUserPermissionRelation  {

    @TableId(value = "user_permission_relation_id", type = IdType.ASSIGN_ID)
    private Integer userPermissionRelationId;

    private Integer userId;

    private Integer permissionId;
}

