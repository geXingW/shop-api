package com.gexingw.shop.modules.ums.controller;

import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.modules.ums.dto.dept.UmsDeptRequestParam;
import com.gexingw.shop.modules.ums.dto.dept.UmsDeptSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.ums.UmsDeptMapper;
import com.gexingw.shop.modules.ums.service.UmsAdminService;
import com.gexingw.shop.modules.ums.service.UmsDeptService;
import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/dept")
@RestController
public class DeptController {
    @Autowired
    UmsDeptService umsDeptService;

    @Autowired
    UmsDeptMapper umsDeptMapper;

    @Autowired
    UmsAdminService umsAdminService;

    /**
     * 部门列表查询
     *
     * @param umsDeptRequestParams
     * @return
     */

    @GetMapping
    @PreAuthorize("@el.check('dept:list')")
    R index(UmsDeptSearchParam umsDeptRequestParams) {
        QueryWrapper<UmsDept> queryWrapper = new QueryWrapper<>();

        // name
        String name = umsDeptRequestParams.getName();
        if (StringUtils.hasText(name)) {
            umsDeptRequestParams.setPid(-1L);
            queryWrapper.like("name", name);
        }

        // pid
        long pid = umsDeptRequestParams.getPid();
        if (pid != -1L) {
            queryWrapper.eq("pid", pid);
        }

        if (umsDeptRequestParams.getEnabled() != null) {
            queryWrapper.eq("enabled", umsDeptRequestParams.getEnabled());
        }

        // 排序
        String[] split = umsDeptRequestParams.getSort().split(",");
        queryWrapper.orderBy(true, "asc".equals(split[1]), split[0]);

        // 分页查询
        IPage<UmsDept> page = new Page<>(umsDeptRequestParams.getPage(), umsDeptRequestParams.getSize());
        IPage<UmsDept> umsDepts = umsDeptMapper.selectPage(page, queryWrapper);

        List<Object> records = new ArrayList<>();
        for (UmsDept umsDept : umsDepts.getRecords()) {
            HashMap<String, Object> deptItem = new HashMap<String, Object>();
            deptItem.put("deptSort", umsDept.getDeptSort());
            deptItem.put("enabled", umsDept.isEnabled());
            deptItem.put("hasChildren", umsDept.getHasChildren());
            deptItem.put("id", umsDept.getId());
            deptItem.put("label", umsDept.getLabel());
            deptItem.put("leaf", umsDept.getLeaf());
            deptItem.put("name", umsDept.getName());
            deptItem.put("pid", umsDept.getPid());
            deptItem.put("subCount", umsDept.getSubCount());

            if (umsDept.getChildren() != null) {
                deptItem.put("children", umsDept.getChildren());
            }
            records.add(deptItem);
        }

        // 拼装返回数据
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("records", records);
        result.put("total", umsDepts.getTotal());
        result.put("size", umsDepts.getSize());
        result.put("page", umsDepts.getCurrent());

        return R.ok(result);
    }

    @GetMapping("level")
    @PreAuthorize("@el.check('dept:list')")
    R level(@RequestParam(name = "pid", required = false, defaultValue = "0") long pid, @RequestParam(name = "sort", required = false, defaultValue = "id,asc") String sort) {
        String[] split = sort.split(",");
        QueryWrapper<UmsDept> queryWrapper = new QueryWrapper<UmsDept>().eq("pid", pid).orderBy(true, "asc".equals(split[1]), split[0]);

        return R.ok(umsDeptMapper.selectList(queryWrapper));
    }

    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public R save(@RequestBody UmsDeptRequestParam deptRequestParam) {
        if (!umsDeptService.save(deptRequestParam)) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
        }

        return R.ok("已保存！");
    }

    @PostMapping("superior")
    @PreAuthorize("@el.check('dept:list')")
    R superior(@RequestBody List<Long> ids) {
        // 获取该节点
        List<UmsDept> depts = umsDeptMapper.selectBatchIds(ids);
        List<Long> pids = depts.stream().map(UmsDept::getPid).collect(Collectors.toList());

        // 获取父级节点
        List<UmsDept> list = umsDeptMapper.selectBatchIds(pids);
        list.stream().map(umsDept -> {
            // 获取子节点
            List<UmsDept> children = umsDeptMapper.selectList(new QueryWrapper<UmsDept>().in("pid", umsDept.getId()));
            if (children.isEmpty()) {
                return umsDept;
            }

            umsDept.setChildren(children);
            return umsDept;
        });

        return R.ok(list);
    }

    /**
     * 获取树形嵌套部门列表
     *
     * @param ids
     * @return
     */
    @PostMapping("tree")
    @PreAuthorize("@el.check('dept:list')")
    R tree(@RequestBody List<Long> ids) {
        List<UmsDept> umsDepts = umsDeptService.tree(0L);

        ArrayList<Object> depts = new ArrayList<>();
        for (UmsDept umsDept : umsDepts) {
            HashMap<String, Object> dept = new HashMap<>();

            dept.put("deptSort", umsDept.getDeptSort());
            dept.put("hasChildren", umsDept.getHasChildren());
            dept.put("id", umsDept.getId());
            dept.put("label", umsDept.getLabel());
            dept.put("leaf", umsDept.getLeaf());
            dept.put("name", umsDept.getName());
            dept.put("pid", umsDept.getPid());
            dept.put("subCount", umsDept.getSubCount());

            if (umsDept.getChildren().size() > 0) {
                dept.put("children", umsDept.getChildren());
            }

            depts.add(dept);
        }

        return R.ok(depts);
    }

    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public R update(@RequestBody UmsDeptRequestParam deptRequestParam) {
        if (!umsDeptService.exist(deptRequestParam.getId())) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "删除失败！");
        }

        if (!umsDeptService.update(deptRequestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        // 清除相关联管理员的数据权限
        List<UmsAdmin> admins = umsDeptService.getDeptAdminsByDeptId(deptRequestParam.getId());
        for (UmsAdmin umsAdmin : admins) {
            umsAdminService.delRedisAdminDataScopeByAdminId(umsAdmin.getId());
            umsDeptService.delRedisAdminDeptByAdminId(umsAdmin.getId());
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public R delete(@RequestBody List<Long> ids) {
        // 清除相关联管理员的数据权限
        List<UmsAdmin> admins = umsDeptService.getDeptAdminsByDeptIds(ids);
        if (!umsDeptService.delete(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        for (UmsAdmin umsAdmin : admins) {
            umsDeptService.delRedisAdminDeptByAdminId(umsAdmin.getId());
        }

        return R.ok("已删除！");
    }
}
