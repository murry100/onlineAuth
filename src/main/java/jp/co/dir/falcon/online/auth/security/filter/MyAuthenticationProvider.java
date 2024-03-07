//package jp.co.dir.falcon.online.auth.security.filter;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import jakarta.annotation.Resource;
//import jp.co.dir.falcon.online.auth.web.entity.SysUser;
//import jp.co.dir.falcon.online.auth.web.mapper.SysUserMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
///**
// * @author ：eleven
// * @date ：Created in 2024/3/7 14:50
// * @description：自定义密码验证规则
// * @version: V1.1
// */
//@Component
//public class MyAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    private SysUserMapper userMapper;
//
//    @Resource
//    BCryptPasswordEncoder passwordEncoder;
//
//
//    //自定义密码验证
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();     //表单提交的用户名
//        String presentedPassword = (String)authentication.getCredentials();     //表单提交的密码
//        SysUser sysUser = userMapper.selectOne(new QueryWrapper<SysUser>().eq("account", username)); // 根据用户名获取用户信息
//        if (StringUtils.isEmpty(sysUser)) {
//            throw new BadCredentialsException("用户名不存在");
//        } else {
//            AccountUser userDeatils = new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));
//
//            if (authentication.getCredentials() == null) {
//                throw new BadCredentialsException("凭证为空");
//            } else if (!passwordEncoder.matches(presentedPassword, sysUser.getPassword())) {
//                System.out.println("encodedPassword:"+presentedPassword);
//                System.out.println("password:"+sysUser.getPassword());
//                throw new BadCredentialsException("密码错误");
//            } else {
//                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDeatils, authentication.getCredentials(), userDeatils.getAuthorities());
//                result.setDetails(authentication.getDetails());
//                return result;
//            }
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//
//
//    //获取用户权限
//    public List<GrantedAuthority> getUserAuthority(Long userId){
//
//        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
//        String authority = userService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....
//
//        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
//    }
//
//    public static void main(String[] args) {
//        String pass = "111111";
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
//        String hashPass = bcryptPasswordEncoder.encode(pass);
//        System.out.println(hashPass);
//
//        boolean f = bcryptPasswordEncoder.matches("111111",hashPass);
//        System.out.println(f);
//    }
//}
//
