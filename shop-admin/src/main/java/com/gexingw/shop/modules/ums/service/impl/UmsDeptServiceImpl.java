package com.gexingw.shop.modules.ums.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.constant.AdminConstant;
import com.gexingw.shop.modules.ums.dto.dept.UmsDeptRequestParam;
import com.gexingw.shop.mapper.ums.UmsAdminMapper;
import com.gexingw.shop.mapper.ums.UmsDeptMapper;
import com.gexingw.shop.modules.ums.service.UmsAdminService;
import com.gexingw.shop.modules.ums.service.UmsDeptService;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsDeptServiceImpl implements UmsDeptService {
    @Autowired
    private UmsDeptMapper umsDeptMapper;

    @Autowired
    private UmsAdminMapper umsAdminMapper;

    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<UmsDept> getAll() {
        return umsDeptMapper.getAll();
    }

    @Override
    public List<UmsDept> getByPid(long pid) {
        return umsDeptMapper.getByPid(pid);
    }

    public List<UmsDept> tree(long pid) {
        List<UmsDept> umsDepts;
        umsDepts = umsDeptMapper.selectList(new QueryWrapper<UmsDept>().eq("pid", pid));
        umsDepts.forEach(umsDept -> {
            // 获取子菜单
            umsDept.setChildren(tree(umsDept.getId()));
        });

        return umsDepts;
    }

    @Override
    public boolean save(UmsDeptRequestParam deptRequestParam) {
        UmsDept umsDept = new UmsDept();
        BeanUtils.copyProperties(deptRequestParam, umsDept);
        if (umsDeptMapper.insert(umsDept) <= 0) {
            return false;
        }

        // 更新父级部门的子菜单数量
        return umsDeptMapper.incrSubCount(umsDept.getPid()) > 0;
    }

    @Override
    @Transactional
    public boolean delete(List<Long> ids) {
        // 获取需要删除菜单的父级菜单
        List<Long> pids = umsDeptMapper.selectBatchIds(ids).stream().map(UmsDept::getPid).collect(Collectors.toList());

        if (umsDeptMapper.deleteBatchIds(ids) <= 0) {
            throw new RuntimeException("删除失败！");
        }

        // 删除成功后，需要更新菜单的子菜单数量
        List<UmsDept> umsDepts = umsDeptMapper.selectList(new QueryWrapper<UmsDept>().in("id", pids));
        for (UmsDept umsDept : umsDepts) {
            umsDept.setSubCount(Math.max(umsDept.getSubCount() - 1, 0));
            if (umsDeptMapper.updateById(umsDept) <= 0) {
                throw new RuntimeException("删除失败！");
            }
        }

        return true;
    }

    @Override
    public boolean exist(Long id) {
        return umsDeptMapper.selectCount(new QueryWrapper<UmsDept>().eq("id", id)) > 0;
    }

    @Override
    @Transactional
    public boolean update(UmsDeptRequestParam deptRequestParam) {
        // 根据ID进行查找
        UmsDept oldValue = umsDeptMapper.selectById(deptRequestParam.getId());

        UmsDept umsDept = new UmsDept();
        BeanUtils.copyProperties(deptRequestParam, umsDept);

        if (umsDeptMapper.updateById(umsDept) <= 0) {
            throw new RuntimeException("更新失败！");
        }

        // 如果旧的pid与信息的不一致，需要更新父级Id的subCount
        if (!oldValue.getPid().equals(umsDept.getPid())) {
            // 增加新父节点的sub_count
            if (umsDeptMapper.incrSubCount(umsDept.getPid()) <= 0) {
                throw new RuntimeException("更新父节点sub count 失败！");
            }

            // 减少之前父节点的sub_count
            UmsDept pMenu = umsDeptMapper.selectById(oldValue.getPid());
            pMenu.setSubCount(Math.max(0, pMenu.getSubCount() - 1));
            if (umsDeptMapper.updateById(pMenu) <= 0) {
                throw new RuntimeException("父节点Sub Count更新失败！");
            }
        }

        return true;
    }

    @Override
    public List<UmsDept> getDeptsByDeptId(List<Long> deptIds) {
        return umsDeptMapper.selectBatchIds(deptIds);
    }

    @Override
    public List<UmsAdmin> getDeptAdminsByDeptId(Long depId) {
        return umsAdminMapper.selectList(new QueryWrapper<UmsAdmin>().eq("dept_id", depId));
    }

    @Override
    public List<UmsAdmin> getDeptAdminsByDeptIds(List<Long> deptIds) {
        return umsAdminMapper.selectList(new QueryWrapper<UmsAdmin>().in("dept_id", deptIds));
    }

    public List<UmsDept> getDeptWithChildrenByPids(List<Long> pids) {
        List<UmsDept> depts = umsDeptMapper.selectList(new QueryWrapper<UmsDept>().in("pid", pids));
        if (depts.size() > 0) {
            depts.addAll(getDeptWithChildrenByPids(depts.stream().map(UmsDept::getId).collect(Collectors.toList())));
        }

        return depts;
    }

    @Override
    public UmsDept getAdminDeptByAdminId(Long adminId) {
        // 查询Redis
        UmsDept umsDept = getRedisAdminDeptByAdminId(adminId);
        if (umsDept != null) {
            return umsDept;
        }

        // 查询数据库
        UmsAdmin umsAdmin = umsAdminService.getAdminDetailByAdminId(adminId);
        umsDept = umsDeptMapper.selectById(umsAdmin.getDeptId());
        if (umsDept != null) {
            setRedisAdminDeptByAdminId(adminId, umsAdmin);
        }

        return umsDept;
    }

    public boolean setRedisAdminDeptByAdminId(Long adminId, UmsAdmin umsAdmin) {
        return redisUtil.set(String.format(AdminConstant.REDIS_ADMIN_DEPT_FORMAT, adminId), umsAdmin);
    }

    public UmsDept getRedisAdminDeptByAdminId(Long adminId) {
        Object redisObj = redisUtil.get(String.format(AdminConstant.REDIS_ADMIN_DEPT_FORMAT, adminId));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(redisObj.toString(), UmsDept.class);
    }

    public void delRedisAdminDeptByAdminId(Long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_DEPT_FORMAT, adminId));
    }
}
