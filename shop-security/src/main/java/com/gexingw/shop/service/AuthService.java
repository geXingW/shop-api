package com.gexingw.shop.service;

import com.gexingw.shop.bo.ums.UmsMenu;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AuthService {

    UserDetails loadUserByUsername(String username);

    List<String> getAdminPermissionsByAdminId(Long adminId);
}
