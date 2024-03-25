package jp.co.dir.falcon.online.auth.security.entity;

import com.google.gson.annotations.Expose;
import jp.co.dir.falcon.online.auth.web.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;
    //ユーザー情報
    private Users user;

    //ユーザーの権利
    private List<String> permissions;

    //SpringSecurityを保存するために必要な権限情報のコレクション
    @Expose(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LogUser(Users user, List<String> permissions) {

        this.user = user;
        this.permissions = permissions;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 許可情報をカプセル化する SimpleGrantedAuthority
        if (authorities != null) {
            return authorities;
        }
        //パーミッション内の文字列型パーミッション情報をGrantedAuthorityオブジェクトに変換し、オーソリティに格納する
        authorities = this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return authorities;

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


