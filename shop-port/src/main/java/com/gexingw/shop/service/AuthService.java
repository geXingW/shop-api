package com.gexingw.shop.service;

import com.gexingw.shop.bo.ums.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    UserDetails loadUserByUsername(String username);

    UmsMember getMemberDetailByUsername(String username);
}
