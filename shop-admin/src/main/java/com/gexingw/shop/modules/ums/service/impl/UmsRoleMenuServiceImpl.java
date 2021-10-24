package com.gexingw.shop.modules.ums.service.impl;

import com.gexingw.shop.bo.ums.UmsRoleMenu;
import com.gexingw.shop.mapper.ums.UmsRoleMenuMapper;
import com.gexingw.shop.modules.ums.service.UmsRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsRoleMenuServiceImpl implements UmsRoleMenuService {

    @Autowired
    UmsRoleMenuMapper umsRoleMenuMapper;

    @Override
    public List<UmsRoleMenu> getListByRoleIds(List<Long> roleIds) {
        return umsRoleMenuMapper.getRoleMenusByRoleIds(roleIds);
    }
}
