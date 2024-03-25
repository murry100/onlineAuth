package jp.co.dir.falcon.online.auth.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jp.co.dir.falcon.online.auth.web.entity.ApiPermissions;
import jp.co.dir.falcon.online.auth.web.entity.Permissions;
import jp.co.dir.falcon.online.auth.web.entity.RolePermissions;
import jp.co.dir.falcon.online.auth.web.entity.Roles;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<Permissions> {


    /**
     * ユーザーIDによるユーザーの権限データのクエリ
     *
     * @param userId
     * @return
     */
    @Select({"<script>" +
            " SELECT rp.* FROM" +
            " users u" +
            " LEFT JOIN user_roles ur ON u.id = ur.user_id" +
            " LEFT JOIN roles r on ur.role_id = r.id" +
            " LEFT JOIN role_permissions rp on r.id = rp.role_id" +
            " WHERE u.id = #{userId}" +
            "</script>"

    })
    List<RolePermissions> selectPermissionList(@Param("userId") Integer userId);

    @Select({"<script>" +
            " SELECT DISTINCT rp.role_id, p.method, p.endpoint FROM" +
            " role_permissions rp" +
            " LEFT JOIN permissions p on rp.function_id = p.function_id and rp.permission_kbn = p.permission_kbn" +
            "</script>"

    })
    List<ApiPermissions> selectPermissionAllList();
}

