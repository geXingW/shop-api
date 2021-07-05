package com.gexingw.shop.bo;

import com.gexingw.shop.bo.ums.UmsAdmin;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UmsAdminDetails implements UserDetails {

    private UmsAdmin umsAdmin;

    private Collection<? extends GrantedAuthority> authorities;

    public UmsAdminDetails(UmsAdmin umsAdmin, Collection<? extends GrantedAuthority> authorities) {
        this.umsAdmin = umsAdmin;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return umsAdmin.isEnabled();
    }

    public boolean isAdmin() {
        return umsAdmin.isAdmin();
    }
}
