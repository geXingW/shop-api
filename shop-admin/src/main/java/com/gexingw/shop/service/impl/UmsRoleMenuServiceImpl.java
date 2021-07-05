package com.gexingw.shop.service.impl;

import com.gexingw.shop.bo.ums.UmsRoleMenu;
import com.gexingw.shop.mapper.UmsRoleMenuMapper;
import com.gexingw.shop.service.UmsRoleMenuService;
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
