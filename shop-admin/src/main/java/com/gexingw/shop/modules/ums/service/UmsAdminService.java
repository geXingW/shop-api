package com.gexingw.shop.modules.ums.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.bo.ums.UmsRole;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminRequestParam;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminSearchParam;
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

    public UmsAdmin getAdminDetailByAdminId(Long adminId);

    boolean updateCenter(UmsAdminRequestParam requestParam) throws Exception;

    IPage<UmsAdmin> queryList(IPage<UmsAdmin> page, UmsAdminSearchParam requestParams);
}
