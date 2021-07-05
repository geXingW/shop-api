package com.gexingw.shop.service;


import com.gexingw.shop.bo.ums.UmsAdminRole;

import java.util.List;

public interface UmsAdminRoleService {
    List<UmsAdminRole> getByUserId(long id);
}
