package jp.co.dir.falcon.online.auth.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.annotation.Resource;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.entity.SysPermission;
import jp.co.dir.falcon.online.auth.web.entity.SysUser;
import jp.co.dir.falcon.online.auth.web.mapper.SysPermissionMapper;
import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        //需要构造出 org.springframework.security.core.userdetails.User 对象并返回


        System.out.println("用户名："+username);
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }

        //根据用户名查询用户
        SysUser user = userMapper.selectOne(new QueryWrapper<SysUser>().eq("account", username));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }



        List<String> permissionsList = new ArrayList<>();

        if (user != null) {
            //获取该用户所拥有的权限
            List<SysPermission> sysPermissions = sysPermissionMapper.selectPermissionList(user.getUserId());

            // 声明用户授权
            sysPermissions.forEach(sysPermission -> {
                permissionsList.add(sysPermission.getPermissionCode());

            });
        }

        //返回用户信息
        return new LogUser(user,permissionsList);

    }


    //这是加密的算法，把加密后的密码update你用户表的数据库用户的密码上
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }


}



