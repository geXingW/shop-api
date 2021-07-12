package com.gexingw.shop.service;

import java.util.List;

public interface AuthService {

//    UserDetails loadUserByUsername(String username);

    List<String> getAdminPermissionsByAdminId(Long adminId);
}
