package com.gexingw.shop.service.impl;

import com.gexingw.shop.bean.ums.UmsAdminRole;
import com.gexingw.shop.mapper.UmsAdminRoleMapper;
import com.gexingw.shop.service.UmsAdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsAdminRoleServiceImpl implements UmsAdminRoleService {
    @Autowired
    UmsAdminRoleMapper umsAdminRoleMapper;

    @Override
    public List<UmsAdminRole> getByUserId(long id) {
        return umsAdminRoleMapper.getRoleByUserId(id);
    }

    public boolean save(long adminId, List<Long> roleIds){
        return umsAdminRoleMapper.insertAdminRoles(adminId, roleIds);
    }
}
