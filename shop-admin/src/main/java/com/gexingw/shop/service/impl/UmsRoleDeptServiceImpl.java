package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bean.ums.UmsRoleDept;
import com.gexingw.shop.mapper.UmsRoleDeptMapper;
import com.gexingw.shop.service.UmsRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsRoleDeptServiceImpl implements UmsRoleDeptService {
    @Autowired
    UmsRoleDeptMapper umsRoleDeptMapper;

    @Override
    public List<Long> getDeptIdsByRoleIds(List<Long> roleIds) {
        List<UmsRoleDept> roleDepts = umsRoleDeptMapper.selectList(new QueryWrapper<UmsRoleDept>().in("role_id", roleIds));
        return roleDepts.stream().map(UmsRoleDept::getDeptId).collect(Collectors.toList());
    }
}
