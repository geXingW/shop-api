package com.gexingw.shop.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthUtil {
    public static UserDetails umsAdmin;

    /**
     * 获取登录用户的Id
     *
     * @return
     */
    public static long getAuthId() {
        UserDetails umsAdmin = getAuthUser();
//        if (umsAdmin == null) {
//            return 0;
//        }
//
//        return umsAdmin.getId();

        return 1L;
    }

    /**
     * 获取登陆用户的详细信息
     *
     * @return
     */
    public static UserDetails getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        return (UserDetails) authentication.getPrincipal();
    }

    public static String getAuthUsername() {
        UserDetails authUser = getAuthUser();
        if (authUser == null) {
            return null;
        }

        return authUser.getUsername();
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getAuthorities();
    }
}

