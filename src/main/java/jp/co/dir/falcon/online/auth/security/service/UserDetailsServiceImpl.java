package jp.co.dir.falcon.online.auth.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.entity.RolePermissions;
import jp.co.dir.falcon.online.auth.web.entity.Users;
import jp.co.dir.falcon.online.auth.web.mapper.SysPermissionMapper;
import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysUserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //構築する必要がある org.springframework.security.core.userdetails.User 反対して返す


        if (username == null || "".equals(username)) {
            throw new RuntimeException("ユーザーを空にすることはできません");
        }

        //ユーザー名に基づいてユーザーをクエリする
        Users user = userMapper.selectOne(new QueryWrapper<Users>().eq("login_id", username));
        if (user == null) {
            throw new RuntimeException("ユーザーは存在しません");
        }


        List<String> permissionsList = new ArrayList<>();

        if (user != null) {
            //このユーザーが所有する権限を取得します
            List<RolePermissions> sysPermissions = sysPermissionMapper.selectPermissionList(user.getUserId());

            // ユーザー権限の宣言
            sysPermissions.forEach(sysPermission -> {
                permissionsList.add(sysPermission.getRoleId().toString());

            });
        }

        //ユーザー情報を返す
        return new LogUser(user, permissionsList);

    }

}



