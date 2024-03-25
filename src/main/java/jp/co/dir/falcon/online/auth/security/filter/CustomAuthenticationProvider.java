package jp.co.dir.falcon.online.auth.security.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jp.co.dir.falcon.online.auth.security.entity.LogUser;
import jp.co.dir.falcon.online.auth.web.entity.RolePermissions;
import jp.co.dir.falcon.online.auth.web.entity.Roles;
import jp.co.dir.falcon.online.auth.web.entity.Users;
import jp.co.dir.falcon.online.auth.web.mapper.SysPermissionMapper;
import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
import jp.co.dir.falcon.online.auth.web.service.OtpService;
import jp.co.dir.falcon.online.auth.web.service.impl.OtpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static Long accountLockMillis;

    @Value("${spring.datasource.accountLockMillis}")
    public void accountLockMillis(Long accountLockMillis) {
        CustomAuthenticationProvider.accountLockMillis = accountLockMillis;
    }

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Users user = userMapper.selectOne(new QueryWrapper<Users>().eq("login_id", name));

        if (user == null) {
            throw new BadCredentialsException("Invalid username");
        }

        Boolean verify = passwordEncoder.matches(password, user.getPassword());

        updateLoginInfo(user, verify);
        // ログインアンロック時間を更新
        checkPasswordErrCount(user, verify);

        String otp = otpService.generateOTP(user.getPhoneNumber());
        System.out.println(otp);

        List<String> permissionsList = new ArrayList<>();
        //このユーザーが所有する権限を取得します
        List<RolePermissions> sysPermissions = sysPermissionMapper.selectPermissionList(user.getUserId());

        // ユーザー権限の宣言
        sysPermissions.forEach(sysPermission -> {
            permissionsList.add(sysPermission.getRoleId().toString());
        });
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(sysPermissions.toString()));

        //ユーザー情報を返す
        return new UsernamePasswordAuthenticationToken(new LogUser(user, permissionsList), password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void checkPasswordErrCount(Users user, Boolean verify) {
        if (user.getUserStatus().equals("01")) {
            if (user.getLoginUnlockDatetime().compareTo(new Date()) > 0) {
                throw new LockedException("Account lock:" + user.getLoginUnlockDatetime());
            } else {
                user.setUserStatus("00");
                user.setLoginErrCnt(0);
            }
        }

        if (!verify) {
            int currNum = user.getLoginErrCnt();
            if (currNum == 2) {
                user.setUserStatus("01");
                user.setLoginErrCnt(0);
                user.setLoginUnlockDatetime(new Date(new Date().getTime() + accountLockMillis));
                userMapper.updateById(user);
                throw new LockedException("Account lock:" + user.getLoginUnlockDatetime());
            } else {
                user.setLoginErrCnt(currNum + 1);
            }
            userMapper.updateById(user);
            throw new BadCredentialsException("Invalid password" + ++currNum);
        }else {
            user.setLoginErrCnt(0);
        }
        userMapper.updateById(user);
    }

    private void updateLoginInfo(Users user, Boolean verify){
        // 累積ﾛｸﾞｲﾝ回数
        user.setLoginCnt(converNullToZero(user.getLoginCnt()) + 1);
        if(verify){
            Date date = new Date();
            // ﾛｸﾞｲﾝOK回数
            user.setLoginOkCnt(converNullToZero(user.getLoginOkCnt()) + 1);
            // 初回ﾛｸﾞｲﾝ日
            if(user.getLoginFirstYmd() == null){
                user.setLoginFirstYmd(date);
            }
            // 最新ログイン日時
            user.setLoginDatetimeLast(new Timestamp(date.getTime()));
        }
    }

    public static int converNullToZero(Integer value){
        return value != null ? value : 0;
    }
}
