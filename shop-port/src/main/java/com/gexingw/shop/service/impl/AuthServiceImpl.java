package com.gexingw.shop.service.impl;

import com.gexingw.shop.bo.UserDetail;
import com.gexingw.shop.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public UserDetails getUserDetailByUserName(String username) {
        UserDetail userDetail = new UserDetail();
        return userDetail;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println(username);

        UserDetail userDetail = new UserDetail();
        return userDetail;
    }
}
