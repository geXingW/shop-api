package com.gexingw.shop.bo;

import com.gexingw.shop.bean.OAuthAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetail implements UserDetails {

    private OAuthAccount authAccount;

    public UserDetail(OAuthAccount authAccount) {
        this.authAccount = authAccount;

        // 获取用户的全新啊
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return authAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return authAccount.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return authAccount.getAccountNonExpired() == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return authAccount.getAccountNonLocked() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return authAccount.getCredentialsNonExpired() == 1;
    }

    @Override
    public boolean isEnabled() {
        return authAccount.getEnabled() == 1;
    }
}
