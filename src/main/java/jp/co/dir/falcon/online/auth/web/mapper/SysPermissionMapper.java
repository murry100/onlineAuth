package jp.co.dir.falcon.online.auth.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jp.co.dir.falcon.online.auth.web.entity.Roles;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<Roles> {


    /**
     * ユーザーIDによるユーザーの権限データのクエリ
     *
     * @param userId
     * @return
     */
    @Select({"<script>" +
            " SELECT p.* FROM" +
            " users u" +
            " LEFT JOIN user_roles r ON u.id = r.user_id" +
            " LEFT JOIN roles p on r.role_id = p.id" +
            " WHERE u.id = #{userId}" +
            "</script>"

    })
    List<Roles> selectPermissionList(@Param("userId") Integer userId);
}

