package com.gexingw.shop.modules.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsAdminRole;
import com.gexingw.shop.bo.ums.UmsRole;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.ums.UmsAdminRoleMapper;
import com.gexingw.shop.mapper.ums.UmsDeptMapper;
import com.gexingw.shop.mapper.ums.UmsRoleMapper;
import com.gexingw.shop.mapper.ums.UmsRoleMenuMapper;
import com.gexingw.shop.modules.ums.dto.role.UmsRoleRequestParam;
import com.gexingw.shop.modules.ums.dto.role.UmsRoleSearchParam;
import com.gexingw.shop.modules.ums.service.UmsAdminService;
import com.gexingw.shop.modules.ums.service.UmsMenuService;
import com.gexingw.shop.modules.ums.service.UmsRoleService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    UmsRoleMapper umsRoleMapper;

    @Autowired
    UmsAdminRoleMapper umsAdminRoleMapper;

    @Autowired
    UmsRoleMenuMapper umsRoleMenuMapper;

    @Autowired
    UmsRoleService umsRoleService;

    @Autowired
    UmsDeptMapper umsDeptMapper;

    @Autowired
    UmsMenuService umsMenuService;

    @Autowired
    UmsAdminService umsAdminService;

    @GetMapping
    @PreAuthorize("@el.check('role:list')")
    public R index(UmsRoleSearchParam roleQueryParam) {
        // 分页信息
        IPage<UmsRole> page = new Page<>(roleQueryParam.getPage(), roleQueryParam.getSize());

        // 查询条件
        QueryWrapper<UmsRole> queryWrapper = new QueryWrapper<UmsRole>();

        // 根据description和name模糊查询
        if (StringUtils.hasText(roleQueryParam.getBlurry())) {
            queryWrapper.and(q -> q.like("name", roleQueryParam.getBlurry())
                    .or()
                    .like("description", roleQueryParam.getBlurry())
            );
        }

        // 根据创建时间查询
        if (roleQueryParam.getCreateTime().length > 1) {
            queryWrapper.between("create_time", roleQueryParam.getCreateTime()[0], roleQueryParam.getCreateTime()[1]);
        }

        // 排序
        if (roleQueryParam.getSort() != null) {
            queryWrapper.orderBy(true, roleQueryParam.isSortAsc(), roleQueryParam.getSortColumn());
        }

        // 查询到的分页结果
        IPage<UmsRole> umsRoleIPage = umsRoleMapper.selectPage(page, queryWrapper);

        // 查询所有
        for (UmsRole umsRole : umsRoleIPage.getRecords()) {
            // 查询所有菜单
            umsRole.setMenus(umsRoleService.getRoleMenus(umsRole.getId()));

            // 查询所有部门
            umsRole.setDepts(umsRoleService.getRoleDepts(umsRole.getId()));
        }

        return R.ok(PageUtil.format(umsRoleIPage));
    }

    @GetMapping("download")
    @PreAuthorize("@el.check('role:list')")
    public void download(UmsRoleSearchParam queryParam, HttpServletResponse response) throws IOException {
        // 分页信息
        IPage<UmsRole> page = new Page<>(queryParam.getPage(), queryParam.getSize());

        // 查询条件
        QueryWrapper<UmsRole> queryWrapper = new QueryWrapper<UmsRole>();

        // 根据description和name模糊查询
        if (StringUtils.hasText(queryParam.getBlurry())) {
            queryWrapper.and(q -> q.like("name", queryParam.getBlurry())
                    .or()
                    .like("description", queryParam.getBlurry())
            );
        }

        // 根据创建时间查询
        if (queryParam.getCreateTime().length > 1) {
            queryWrapper.between("create_time", queryParam.getCreateTime()[0], queryParam.getCreateTime()[1]);
        }

        // 排序
        queryWrapper.orderBy(true, queryParam.isSortAsc(), queryParam.getSortColumn());

        // 查询到的分页结果
        IPage<UmsRole> umsRoleIPage = umsRoleMapper.selectPage(page, queryWrapper);

        List<UmsRole> records = umsRoleIPage.getRecords();

        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsRole umsRole : records) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("ID", umsRole.getId());
            item.put("名称", umsRole.getName());
            item.put("数据权限", umsRole.getDataScope());
            item.put("描述", umsRole.getDescription());
            list.add(item);
        }

        FileUtil.downloadExcel(list, response);
    }

    @GetMapping("{id}")
    @PreAuthorize("@el.check('role:list')")
    public R show(@PathVariable Long id) {
        // 检查该资源是否存在
        UmsRole role = umsRoleService.findById(id);
        if (role == null) {
            return R.failure(RespCode.ADMIN_ROLE_NOT_EXIST);
        }

        // 获取角色菜单信息
        role.setMenus(umsRoleService.getRoleMenus(id));

        // 获取角色部门信息
        role.setDepts(umsRoleService.getRoleDepts(id));

        return R.ok(role);
    }

    @PostMapping
    @PreAuthorize("@el.check('role:add')")
    public R save(@RequestBody UmsRoleRequestParam roleRequestParam) {
        if (!umsRoleService.checkLevel(roleRequestParam)) {
            return R.failure(RespCode.OPERATION_DENY);
        }

        return R.ok(RespCode.ADMIN_ROLE_CREATED, umsRoleService.save(roleRequestParam));
    }

    @PutMapping
    @PreAuthorize("@el.check('role:edit')")
    public R update(@RequestBody UmsRoleRequestParam roleRequestParam) {
        if (!umsRoleService.checkLevel(roleRequestParam)) {
            return R.failure(RespCode.OPERATION_DENY);
        }

        // 检查该资源是否存在
        if (!umsRoleService.isIdExist(roleRequestParam.getId())) {
            return R.failure(RespCode.ADMIN_ROLE_NOT_EXIST);
        }

        if (!umsRoleService.update(roleRequestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // 删除redis中存储的用户菜单信息
        List<Long> roleUserIds = umsRoleService.getRoleUserIds(roleRequestParam.getId());
        for (Long userId : roleUserIds) {
            // 更新角色信息
            umsRoleService.delRedisAdminRolesByAdminId(userId);
            // 更新菜单缓存
            umsMenuService.delRedisAdminMenuByAdminId(userId);
            // 更新权限缓存
            umsMenuService.delRedisAdminPermissionByAdminId(userId);
            // 更新数据权限
            umsAdminService.delRedisAdminDataScopeByAdminId(userId);
        }

        return R.ok(RespCode.ADMIN_ROLE_UPDATED);
    }

    @PutMapping("menu")
    @PreAuthorize("@el.check('role:edit')")
    public R updateMenu(@RequestBody UmsRoleRequestParam roleRequestParam) {
        // 检查该资源是否存在
        if (!umsRoleService.isIdExist(roleRequestParam.getId())) {
            return R.failure(RespCode.ADMIN_ROLE_NOT_EXIST);
        }

        if (!umsRoleService.updateRoleMenu(roleRequestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE, "菜单更新失败！");
        }

        // 删除redis中存储的用户菜单信息
        List<Long> roleUserIds = umsRoleService.getRoleUserIds(roleRequestParam.getId());
        for (Long userId : roleUserIds) {
            // 更新角色信息
            umsRoleService.delRedisAdminRolesByAdminId(userId);
            // 更新菜单缓存
            umsMenuService.delRedisAdminMenuByAdminId(userId);
            // 更新权限缓存
            umsMenuService.delRedisAdminPermissionByAdminId(userId);
            // 更新数据权限
            umsAdminService.delRedisAdminDataScopeByAdminId(userId);
        }

        return R.ok(RespCode.MENU_UPDATED);
    }

    @GetMapping("level")
    @PreAuthorize("@el.check('role:list')")
    public R withMenu() {
        // 获取用户所有角色
        long authId = AuthUtil.getAuthId();
        List<UmsAdminRole> adminRoles = umsAdminRoleMapper.selectList(new QueryWrapper<UmsAdminRole>().eq("admin_id", authId));
        List<Long> roleIds = adminRoles.stream().map(UmsAdminRole::getRoleId).collect(Collectors.toList());

        //获取用户所有角色等级
        List<Integer> roleLevels = umsRoleMapper.selectList(new QueryWrapper<UmsRole>()).stream().map(UmsRole::getLevel).collect(Collectors.toList());
        HashMap<String, Integer> result = new HashMap<>();

        return R.ok(Collections.min(roleLevels));
    }

    @DeleteMapping
    @PreAuthorize("@el.check('role:del')")
    public R delete(@RequestBody List<Long> ids) {
        if (!umsRoleService.batchDeleteByIds(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        List<Long> adminIds = umsRoleService.getAdminIdsByRoleIds(ids);
        for (Long adminId : adminIds) {
            // 更新角色信息
            umsRoleService.delRedisAdminRolesByAdminId(adminId);
            // 更新菜单缓存
            umsMenuService.delRedisAdminMenuByAdminId(adminId);
            // 更新权限缓存
            umsMenuService.delRedisAdminPermissionByAdminId(adminId);
            // 更新数据权限
            umsAdminService.delRedisAdminDataScopeByAdminId(adminId);
        }

        return R.ok(RespCode.ADMIN_ROLE_DELETED);
    }
}

