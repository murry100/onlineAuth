package jp.co.dir.falcon.online.auth.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jp.co.dir.falcon.online.auth.web.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermission> {


    /**
     * 通过用户id查询用户的权限数据
     * @param userId
     * @return
     */
    @Select({"<script>"+
            " SELECT p.* FROM"+
            " sys_user u"+
            " LEFT JOIN sys_user_permission_relation r ON u.user_id = r.user_id"+
            " LEFT JOIN sys_permission p on r.permission_id = p.permission_id"+
            " WHERE u.user_id = #{userId}"+
            "</script>"

    })
    List<SysPermission> selectPermissionList(@Param("userId") Integer userId);
}

