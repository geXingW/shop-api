package com.gexingw.shop.service;

import com.gexingw.shop.bo.ums.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    UserDetails loadUserByUsername(String username);

    boolean isPasswdMatch(String password1, String password2);

    public UmsMember getMemberDetailByUsername(String username);
}
