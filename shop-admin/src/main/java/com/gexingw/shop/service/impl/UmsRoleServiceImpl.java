package com.gexingw.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.ums.*;
import com.gexingw.shop.constant.AdminConstant;
import com.gexingw.shop.dto.role.UmsRoleRequestParam;
import com.gexingw.shop.mapper.UmsAdminRoleMapper;
import com.gexingw.shop.mapper.UmsRoleDeptMapper;
import com.gexingw.shop.mapper.UmsRoleMapper;
import com.gexingw.shop.mapper.UmsRoleMenuMapper;
import com.gexingw.shop.service.UmsRoleService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    UmsRoleMapper umsRoleMapper;

    @Autowired
    UmsRoleMenuMapper umsRoleMenuMapper;

    @Autowired
    UmsRoleDeptMapper umsRoleDeptMapper;

    @Autowired
    UmsAdminRoleMapper umsAdminRoleMapper;

    @Autowired
    RedisUtil redisUtil;

    public UmsRole findById(long id) {
        return umsRoleMapper.findById(id);
    }

    public List<UmsRole> getByIds(List<Long> ids) {
        return umsRoleMapper.getByIds(ids);
    }

    @Override
    public List<UmsMenu> getRoleMenus(Long roleId) {
        return umsRoleMapper.getRoleMenus(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UmsRoleRequestParam roleRequestParam) {
        UmsRole umsRole = new UmsRole();
        BeanUtils.copyProperties(roleRequestParam, umsRole);

        // 保存角色信息
        if (umsRoleMapper.insert(umsRole) <= 0) {
            throw new RuntimeException("角色保存失败！");
        }

        // 保存角色所属部门信息
        if (roleRequestParam.getDepts().size() > 0) {
            if (umsRoleDeptMapper.saveBatch(roleRequestParam.getId(), roleRequestParam.getDepts()) <= 0) {
                throw new RuntimeException("角色所属部门保存失败！");
            }
        }

        if (!saveRoleMenus(roleRequestParam)) {
            throw new RuntimeException("角色菜单信息保存失败！");
        }

        return umsRole.getId();
    }

    @Override
    public List<UmsDept> getRoleDepts(Long roleId) {
        return umsRoleMapper.getRoleDepts(roleId);
    }

    /**
     * 查询该ID是否存在
     *
     * @param id
     * @return
     */
    @Override
    public boolean isIdExist(Long id) {
        return umsRoleMapper.selectCount(new QueryWrapper<UmsRole>().eq("id", id)) > 0;
    }

    @Override
    @Transactional
    public boolean update(UmsRoleRequestParam roleRequestParam) {
        // 更新角色信息
        UmsRole umsRole = new UmsRole();
        BeanUtils.copyProperties(roleRequestParam, umsRole);
        umsRoleMapper.update(umsRole, new QueryWrapper<UmsRole>().eq("id", roleRequestParam.getId()));

        // 删除角色所属部门信息
        umsRoleDeptMapper.delete(new QueryWrapper<UmsRoleDept>().eq("role_id", roleRequestParam.getId()));
        // 重新写入角色所属部门信息
        if (roleRequestParam.getDepts().size() > 0) {
            umsRoleDeptMapper.saveBatch(umsRole.getId(), roleRequestParam.getDepts());
        }

        // 更新角色菜单信息
        updateRoleMenu(roleRequestParam);

        return true;
    }

    @Transactional
    public boolean updateRoleMenu(UmsRoleRequestParam roleRequestParam) {
        // 删除角色菜单信息
        umsRoleMenuMapper.delete(new QueryWrapper<UmsRoleMenu>().eq("role_id", roleRequestParam.getId()));

        return saveRoleMenus(roleRequestParam);
    }

    @Transactional
    public boolean saveRoleMenus(UmsRoleRequestParam roleRequestParam) {
        List<UmsMenu> menus = roleRequestParam.getMenus().stream().filter(Objects::nonNull).collect(Collectors.toList());
        return umsRoleMenuMapper.saveBatch(roleRequestParam.getId(), menus) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteByIds(List<Long> ids) {
        // 删除角色部门信息
        umsRoleDeptMapper.delete(new QueryWrapper<UmsRoleDept>().in("role_id", ids));

        // 删除角色菜单信息
        umsRoleMenuMapper.delete(new QueryWrapper<UmsRoleMenu>().in("role_id", ids));

        // 删除角色信息
        return umsRoleMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Long> getRoleUserIds(Long id) {
        // 查找所有绑定了该角色的用户ID
        List<UmsAdminRole> adminRoles = umsAdminRoleMapper.selectList(new QueryWrapper<UmsAdminRole>().eq("role_id", id));
        return adminRoles.stream().map(UmsAdminRole::getAdminId).collect(Collectors.toList());
    }

    @Override
    public boolean checkLevel(UmsRoleRequestParam roleRequestParam) {
        long adminId = AuthUtil.getAuthId();
        // 获取当前用户的角色Id
        QueryWrapper<UmsAdminRole> queryWrapper1 = new QueryWrapper<UmsAdminRole>().eq("admin_id", adminId);
        List<Long> roleIds = umsAdminRoleMapper.selectList(queryWrapper1).stream().map(UmsAdminRole::getRoleId).collect(Collectors.toList());

        //获取当前用户的最小权限
        QueryWrapper<UmsRole> queryWrapper2 = new QueryWrapper<UmsRole>().in("id", roleIds);
        List<Integer> roleLvls = umsRoleMapper.selectList(queryWrapper2).stream().map(UmsRole::getLevel).collect(Collectors.toList());

        return Collections.min(roleLvls) <= roleRequestParam.getLevel();
    }

    @Override
    public List<UmsRole> getAdminRolesByAdminId(Long id) {
        // 尝试从Redis中获取
        List<UmsRole> roles = getRedisAdminRolesByAdminId(id);
        if (roles != null) {
            return roles;
        }

        // 如果Redis中没有，从数据库查询
        List<UmsAdminRole> adminRoles = umsAdminRoleMapper.selectList(new QueryWrapper<UmsAdminRole>().eq("admin_id", id));
        List<Long> roleIds = adminRoles.stream().map(UmsAdminRole::getRoleId).collect(Collectors.toList());
        roles = umsRoleMapper.selectBatchIds(roleIds);

        // 长查询到的角色信息，写入redis
        setRedisAdminRolesByAdminId(id, roles);
        return roles;
    }

    public boolean setRedisAdminRolesByAdminId(Long adminId, List<UmsRole> roles) {
        String redisKey = String.format(AdminConstant.REDIS_ADMIN_ROLES_FORMAT, adminId);
        for (UmsRole umsRole : roles) {
            redisUtil.lSet(redisKey, umsRole);
        }

        return true;
    }

    public List<UmsRole> getRedisAdminRolesByAdminId(Long adminId) {
        String redisKey = String.format(AdminConstant.REDIS_ADMIN_ROLES_FORMAT, adminId);
        if (!redisUtil.hasKey(redisKey)) {
            return null;
        }

        List<Object> redisObjs = redisUtil.lGet(redisKey, 0, -1);

        TypeReference<List<UmsRole>> typeReference = new TypeReference<List<UmsRole>>() {
        };

        return JSON.parseObject(JSON.toJSONString(redisObjs), typeReference);
    }

    public void delRedisAdminRolesByAdminId(Long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_ROLES_FORMAT, adminId));
    }

    @Override
    public List<Long> getAdminIdsByRoleIds(List<Long> ids) {
        List<UmsAdminRole> adminRoles = umsAdminRoleMapper.selectList(new QueryWrapper<UmsAdminRole>().in("role_id", ids));
        return adminRoles.stream().map(UmsAdminRole::getAdminId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getSuperAdminRoleIds() {
        return umsRoleMapper.selectList(new QueryWrapper<UmsRole>().eq("level", 1))
                .stream().map(UmsRole::getId).collect(Collectors.toList());
    }
}
