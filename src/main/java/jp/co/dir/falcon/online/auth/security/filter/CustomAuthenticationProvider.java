package jp.co.dir.falcon.online.auth.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jp.co.dir.falcon.online.auth.common.api.ApiCode;
import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.common.utils.JwtUtils;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.entity.SysPermission;
import jp.co.dir.falcon.online.auth.web.entity.SysUser;
import jp.co.dir.falcon.online.auth.web.mapper.SysPermissionMapper;
import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        SysUser user = userMapper.selectOne(new QueryWrapper<SysUser>().eq("account", name));

        if (user == null) {
            throw new BadCredentialsException("Invalid username");
        }

        SysUser sysUser = new SysUser();
        sysUser.setUserId(user.getUserId());
        if (user.getAccountNotLocked().equals(false)){
            if(user.getAllowAt().compareTo(new Date()) > 0){
                throw new BadCredentialsException("Account lock:" + user.getAllowAt());
            }else {
                sysUser.setAccountNotLocked(true);
                userMapper.updateById(sysUser);
            }
        }

        if (!passwordEncoder.matches(password, user.getPassword())){
            int currNum = user.getErrorNum();
            if(currNum == 2){
                long time = 1*60*1000;
                sysUser.setAccountNotLocked(false);
                sysUser.setErrorNum(0);
                sysUser.setAllowAt(new Date(new Date() .getTime() + time));
            }else {
                sysUser.setErrorNum(currNum+1);
            }
            userMapper.updateById(sysUser);
            throw new BadCredentialsException("Invalid password" + ++currNum);
        }

        List<String> permissionsList = new ArrayList<>();
        //获取该用户所拥有的权限
        List<SysPermission> sysPermissions = sysPermissionMapper.selectPermissionList(user.getUserId());

        // 声明用户授权
        sysPermissions.forEach(sysPermission -> {
            permissionsList.add(sysPermission.getPermissionCode());

        });
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(sysPermissions.toString()));

        //返回用户信息
        return new UsernamePasswordAuthenticationToken(new LogUser(user,permissionsList), password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
