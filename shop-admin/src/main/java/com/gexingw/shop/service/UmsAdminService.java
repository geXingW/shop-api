package com.gexingw.shop.service;


import com.gexingw.shop.bean.ums.UmsAdmin;
import com.gexingw.shop.bean.ums.UmsMenu;
import com.gexingw.shop.bean.ums.UmsRole;
import com.gexingw.shop.dto.admin.UmsAdminRequestParam;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface UmsAdminService {

    UmsAdmin findByUserName(String username);

    List<UmsRole> getUserRolesById(Long id);

    List<UmsMenu> getUserMenusById(Long id);

    List<UmsAdmin> getList();

    long save(UmsAdminRequestParam umsAdminRequestParam);

    boolean update(UmsAdminRequestParam umsAdminRequestParam);

    public boolean delByIds(Set<Long> ids);

    public boolean exist(Long id);

    UmsAdmin findById(Long adminId);

    UmsAdmin getRedisAdminDetailByAdminId(Long adminId);

    boolean setRedisAdminDetailByAdminId(Long adminId, UmsAdmin umsAdmin);

    UmsAdmin getRedisAdminDetailByAdminName(String username);

    boolean setRedisAdminDetailByAdminName(String username, UmsAdmin umsAdmin);

    void delRedisAdminDetailByAdminName(String username);

    boolean checkLevel(UmsAdminRequestParam umsAdminRequestParam);

    void delRedisAdminDetailByAdminId(long adminId);

    List<String> getUserPermissions(Long adminId);

    List<Long> getUserDataScope(Long adminId);

    public List<Long> getRedisAminDataScopeByAdminId(Long adminId);

    public boolean setRedisAdminDataScopeByAdminId(Long adminId, List<Long> dataScope);

    public void delRedisAdminDataScopeByAdminId(Long adminId);

    UmsAdmin getAdminDetailByAdminName(String authUsername);

    UserDetails loadUserByUsername(String username);
}