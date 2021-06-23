package com.gexingw.shop.service;


import com.gexingw.shop.bean.ums.UmsAdminRole;

import java.util.List;

public interface UmsAdminRoleService {
    List<UmsAdminRole> getByUserId(long id);
}
