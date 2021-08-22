package com.gexingw.shop.service;

import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.bo.ums.UmsRole;
import com.gexingw.shop.dto.role.UmsRoleRequestParam;

import java.util.List;

public interface UmsRoleService {

    UmsRole findById(long id);

    List<UmsRole> getByIds(List<Long> id);

    List<UmsMenu> getRoleMenus(Long roleId);

    Long save(UmsRoleRequestParam roleRequestParam);

    List<UmsDept> getRoleDepts(Long roleId);

    boolean isIdExist(Long id);

    boolean update(UmsRoleRequestParam roleRequestParam);

    public boolean updateRoleMenu(UmsRoleRequestParam roleRequestParam);

    public boolean saveRoleMenus(UmsRoleRequestParam roleRequestParam);

    boolean batchDeleteByIds(List<Long> ids);

    List<Long> getRoleUserIds(Long id);

    boolean checkLevel(UmsRoleRequestParam roleRequestParam);

    List<UmsRole> getAdminRolesByAdminId(Long id);

    public boolean setRedisAdminRolesByAdminId(Long id, List<UmsRole> roles);

    public List<UmsRole> getRedisAdminRolesByAdminId(Long id);

    public void delRedisAdminRolesByAdminId(Long adminId);

    List<Long> getAdminIdsByRoleIds(List<Long> ids);

    List<Long> getSuperAdminRoleIds();
}
