package com.gexingw.shop.modules.ums.service;


import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.modules.ums.dto.menu.UmsMenuRequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author jobob
 * @since 2021-02-16
 */
public interface UmsMenuService {
    public UmsMenu findById(long id);

    public List<UmsMenu> getByIds(List<Long> ids);

    public List<UmsMenu> getByPidAndIds(long pid, List<Long> ids);

    public List<UmsMenu> getByPid(long pid);

    public Map<String, Object> formatMenuForFront(UmsMenu umsMenu);

    public UmsMenu findChild(Long id);

    public List<UmsMenu> getChildren(Long id, List<UmsMenu> menus);

    Long save(UmsMenuRequestParam requestParam);

    boolean update(UmsMenuRequestParam menuRequestParam);

    boolean delete(List<Long> ids);

    public List<Map<String, Object>> getAdminMenusFromRedisByAdminId(Long adminId);

    boolean setAdminMenusFromRedisByAdminId(Long adminId, List<Map<String, Object>> menus);

    void delRedisAdminMenuByAdminId(Long adminId);

    public List<String> getAdminPermissionFromRedisByAdminId(Long adminId);

    boolean setRedisAdminPermissionByAdminId(Long adminId, String permission);

    void delRedisAdminPermissionByAdminId(Long adminId);

    boolean incrParentMenuSubCount(Long pid);

    UmsMenu getMenuById(Long id);

    boolean decrParentMenuSubCount(Long pid);

    List<Long> delRoleMenusByMenuId(Long menuId);

    boolean delAdminMenuAndPermissionCacheByRoleIds(List<Long> roleIds);

    List<Long> getMenuRoleIdsListByMenuId(Long menuId);

    boolean addMenuToAdminRole(Long menuId);
}

