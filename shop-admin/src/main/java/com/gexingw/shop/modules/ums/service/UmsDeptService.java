package com.gexingw.shop.modules.ums.service;


import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.modules.ums.dto.dept.UmsDeptRequestParam;

import java.util.List;

public interface UmsDeptService {
    List<UmsDept> getAll();

    List<UmsDept> getByPid(long pid);

    public List<UmsDept> tree(long pid);

    boolean save(UmsDeptRequestParam deptRequestParam);

    boolean delete(List<Long> ids);

    boolean exist(Long id);

    boolean update(UmsDeptRequestParam deptRequestParam);

    List<UmsDept> getDeptsByDeptId(List<Long> deptIds);

    List<UmsAdmin> getDeptAdminsByDeptId(Long depId);

    List<UmsAdmin> getDeptAdminsByDeptIds(List<Long> deptIds);

    public List<UmsDept> getDeptWithChildrenByPids(List<Long> pids);

    UmsDept getAdminDeptByAdminId(Long id);

    public UmsDept getRedisAdminDeptByAdminId(Long adminId);

    public boolean setRedisAdminDeptByAdminId(Long adminId, UmsAdmin umsAdmin);

    public void delRedisAdminDeptByAdminId(Long adminId);
}
