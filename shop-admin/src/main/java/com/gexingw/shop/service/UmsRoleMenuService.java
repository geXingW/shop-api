package com.gexingw.shop.service;


import com.gexingw.shop.bo.ums.UmsRoleMenu;

import java.util.List;

public interface UmsRoleMenuService {
    public List<UmsRoleMenu> getListByRoleIds(List<Long> roleIds);
}
