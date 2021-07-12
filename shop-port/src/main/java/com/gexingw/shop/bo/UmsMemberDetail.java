package com.gexingw.shop.bo;

import com.gexingw.shop.bo.ums.UmsMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UmsMemberDetail implements UserDetails {

    private UmsMember umsMember;

    private Collection<? extends GrantedAuthority> authorities;

    public UmsMemberDetail(UmsMember umsMember, Collection<? extends GrantedAuthority> authorities) {
        this.umsMember = umsMember;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.umsMember.getPassword();
    }

    @Override
    public String getUsername() {
        return this.umsMember.getUsername();
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
