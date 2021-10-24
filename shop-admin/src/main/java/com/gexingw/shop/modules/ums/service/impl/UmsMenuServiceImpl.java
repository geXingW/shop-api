package com.gexingw.shop.modules.ums.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gexingw.shop.bo.ums.UmsAdminRole;
import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.bo.ums.UmsRoleMenu;
import com.gexingw.shop.constant.AdminConstant;
import com.gexingw.shop.modules.ums.dto.menu.UmsMenuRequestParam;
import com.gexingw.shop.mapper.ums.UmsAdminRoleMapper;
import com.gexingw.shop.mapper.ums.UmsMenuMapper;
import com.gexingw.shop.mapper.ums.UmsRoleMenuMapper;
import com.gexingw.shop.modules.ums.service.UmsMenuService;
import com.gexingw.shop.modules.ums.service.UmsRoleService;
import com.gexingw.shop.utils.RedisUtil;
import com.gexingw.shop.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-02-16
 */
@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Autowired
    UmsMenuMapper umsMenuMapper;

    @Autowired
    UmsRoleMenuMapper roleMenuMapper;

    @Autowired
    UmsAdminRoleMapper adminRoleMapper;

    @Autowired
    UmsRoleService roleService;

    @Autowired
    StringUtil strUtil;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public UmsMenu findById(long id) {
        return umsMenuMapper.findById(id);
    }

    public List<UmsMenu> getByIds(List<Long> ids) {
        return umsMenuMapper.getMenusByIds(ids);
    }

    public List<UmsMenu> getByPid(long pid) {
        return umsMenuMapper.getByPid(pid);
    }

    public List<UmsMenu> getByPidAndIds(long pid, List<Long> ids) {
        return umsMenuMapper.getByPidAndIds(pid, ids);
    }

    public Map<String, Object> formatMenuForFront(UmsMenu umsMenu) {
        // 拼装Meta
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("icon", umsMenu.getIcon());
        meta.put("noCache", umsMenu.isCache());
        meta.put("title", umsMenu.getTitle());

        // 拼装返回值
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", umsMenu.getId());
        result.put("hidden", umsMenu.isHidden());
        result.put("name", strUtil.upperCase(umsMenu.getPath()));
        result.put("path", umsMenu.getPid() == 0 ? "/" + umsMenu.getPath() : umsMenu.getPath());
        result.put("meta", meta);

        if (umsMenu.getPid() == 0) {
            result.put("alwaysShow", true);
            result.put("redirect", "noredirect");
            result.put("component", StringUtils.isBlank(umsMenu.getComponent()) ? "Layout" : umsMenu.getComponent());
        } else if (umsMenu.getType() == 0) {
            result.put("component", StringUtils.isBlank(umsMenu.getComponent()) ? "ParentView" : umsMenu.getComponent());
        } else if (StringUtils.isNotBlank(umsMenu.getComponent())) {
            result.put("component", umsMenu.getComponent());
        }

        return result;
    }

    @Override
    public UmsMenu findChild(Long id) {
        return umsMenuMapper.findById(id);
    }

    @Override
    public List<UmsMenu> getChildren(Long id, List<UmsMenu> menus) {
        List<UmsMenu> childMenus = umsMenuMapper.selectList(new QueryWrapper<UmsMenu>().eq("pid", id));

        for (UmsMenu childMenu : childMenus) {
            menus.add(childMenu);
            getChildren(childMenu.getId(), menus);
        }

        return menus;
    }

    @Override
    public Long save(UmsMenuRequestParam requestParam) {
        UmsMenu umsMenu = new UmsMenu();
        BeanUtils.copyProperties(requestParam, umsMenu);

        if (umsMenuMapper.insert(umsMenu) <= 0) {
            throw new RuntimeException("保存失败！");
        }

        return umsMenu.getId();
    }

    @Override
    public boolean update(UmsMenuRequestParam menuRequestParam) {
        UmsMenu umsMenu = new UmsMenu();
        BeanUtils.copyProperties(menuRequestParam, umsMenu);

        return umsMenuMapper.updateById(umsMenu) > 0;
    }

    @Override
    public boolean delete(List<Long> ids) {
        return umsMenuMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Map<String, Object>> getAdminMenusFromRedisByAdminId(Long adminId) {
        Object jsonObj = redisUtil.get(String.format(AdminConstant.REDIS_ADMIN_MENUS_FORMAT, adminId));
        if (jsonObj == null) {
            return null;
        }
        TypeReference<List<Map<String, Object>>> reference = new TypeReference<List<Map<String, Object>>>() {
        };

        return JSON.parseObject(jsonObj.toString(), reference);
    }

    @Override
    public boolean setAdminMenusFromRedisByAdminId(Long adminId, List<Map<String, Object>> menus) {
        String jsonString = JSON.toJSONString(menus);
        return redisUtil.set(String.format(AdminConstant.REDIS_ADMIN_MENUS_FORMAT, adminId), jsonString);
    }

    @Override
    public void delRedisAdminMenuByAdminId(Long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_MENUS_FORMAT, adminId));
    }

    @Override
    public List<String> getAdminPermissionFromRedisByAdminId(Long adminId) {
        String redisKey = String.format(AdminConstant.REDIS_ADMIN_PERMISSIONS_FORMAT, adminId);
        if (!redisUtil.hasKey(redisKey)) {
            return null;
        }
        List<Object> menusList = redisUtil.lGet(redisKey, 0, -1);
        return menusList.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public boolean setRedisAdminPermissionByAdminId(Long adminId, String permission) {
        return redisUtil.lSet(String.format(AdminConstant.REDIS_ADMIN_PERMISSIONS_FORMAT, adminId), permission, -1);
    }

    @Override
    public void delRedisAdminPermissionByAdminId(Long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_PERMISSIONS_FORMAT, adminId));
    }

    @Override
    public UmsMenu getMenuById(Long id) {
        return umsMenuMapper.selectById(id);
    }

    @Override
    public boolean incrParentMenuSubCount(Long pid) {
        // pid为零代表顶级菜单，无需更新
        if (pid == 0) {
            return true;
        }

        // 查找父级菜单
        UmsMenu parentMenu = umsMenuMapper.selectById(pid);
        if (parentMenu == null) {
            return false;
        }

        // 更新父级菜单的 subcount
        parentMenu.incrSubCount(1);

        return umsMenuMapper.updateById(parentMenu) > 0;
    }

    @Override
    public boolean decrParentMenuSubCount(Long pid) {
        // pid为零代表顶级菜单，无需更新
        if (pid == 0) {
            return true;
        }

        // 查找父级菜单
        UmsMenu parentMenu = umsMenuMapper.selectById(pid);
        if (parentMenu == null) {
            return false;
        }

        // 更新父级菜单的 subcount
        parentMenu.decrSubCount(1);

        return umsMenuMapper.updateById(parentMenu) > 0;
    }

    @Override
    public List<Long> delRoleMenusByMenuId(Long menuId) {
        // 根据menuId 查询
        List<Long> roleIds = getMenuRoleIdsListByMenuId(menuId);

        if (roleMenuMapper.delete(new QueryWrapper<UmsRoleMenu>().eq("menu_id", menuId)) > 0) {
            return new ArrayList<>();
        }

        return roleIds;
    }

    @Override
    public boolean delAdminMenuAndPermissionCacheByRoleIds(List<Long> roleIds) {
        if (roleIds.size() == 0) {
            return false;
        }

        // 查询所有与菜单绑定的管理员Id
        List<Long> adminIds = adminRoleMapper.selectList(new QueryWrapper<UmsAdminRole>().in("role_id", roleIds))
                .stream().map(UmsAdminRole::getAdminId).collect(Collectors.toList());

        for (Long adminId : adminIds) {
            delRedisAdminMenuByAdminId(adminId);    // 清除菜单缓存
            delRedisAdminPermissionByAdminId(adminId);  // 清除权限缓存
        }

        return true;
    }

    @Override
    public List<Long> getMenuRoleIdsListByMenuId(Long menuId) {
        return roleMenuMapper.selectList(new QueryWrapper<UmsRoleMenu>().eq("menu_id", menuId))
                .stream().map(UmsRoleMenu::getRoleId).collect(Collectors.toList());
    }

    @Override
    public boolean addMenuToAdminRole(Long menuId) {
        // 查询超级管理员的角色Id，假定所有超级管理员的level为1
        List<Long> superAdminRoleIds = roleService.getSuperAdminRoleIds();
        for (Long superAdminRoleId : superAdminRoleIds) {
            UmsRoleMenu umsRoleMenu = new UmsRoleMenu(menuId, superAdminRoleId);
            if (roleMenuMapper.insert(umsRoleMenu) <= 0) {
                return false;
            }
        }

        return true;
    }

}

