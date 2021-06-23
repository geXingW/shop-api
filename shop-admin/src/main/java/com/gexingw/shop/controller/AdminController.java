package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.ums.UmsAdmin;
import com.gexingw.shop.dto.admin.UmsAdminRequestParam;
import com.gexingw.shop.dto.admin.UmsAdminSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.UmsAdminMapper;
import com.gexingw.shop.service.UmsAdminService;
import com.gexingw.shop.service.UmsMenuService;
import com.gexingw.shop.service.UmsRoleService;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private UmsAdminMapper umsAdminMapper;

    @Autowired
    UmsMenuService umsMenuService;

    @Autowired
    UmsRoleService umsRoleService;

    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public R index(UmsAdminSearchParam requestParams) {
        IPage<UmsAdmin> page = umsAdminMapper.queryList(new Page<>(requestParams.getPage(), requestParams.getSize()), requestParams);
        return R.ok(PageUtil.format(page));
    }

    @GetMapping("download")
    @PreAuthorize("@el.check('user:list')")
    public void download(UmsAdminSearchParam searchParams, HttpServletResponse response) throws IOException {
        IPage<UmsAdmin> page = umsAdminMapper.queryList(new Page<>(searchParams.getPage(), searchParams.getSize()), searchParams);
        List<UmsAdmin> records = page.getRecords();

        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsAdmin umsAdmin : records) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("ID", umsAdmin.getId());
            item.put("用户名", umsAdmin.getUsername());
            item.put("昵称", umsAdmin.getNickName());
            item.put("性别", umsAdmin.getGender());
            item.put("电话", umsAdmin.getPhone());
            list.add(item);
        }

        FileUtil.downloadExcel(list, response);
    }

    @PostMapping()
    @PreAuthorize("@el.check('user:add')")
    public R add(@RequestBody UmsAdminRequestParam umsAdminRequestParam) {
        if (!umsAdminService.checkLevel(umsAdminRequestParam)) {
            return R.ok("权限不足！");
        }

        return umsAdminService.save(umsAdminRequestParam) > 0 ? R.ok("添加成功！") : R.ok("添加失败！");
    }

    @PutMapping()
    @PreAuthorize("@el.check('user:edit')")
    public R update(@RequestBody UmsAdminRequestParam umsAdminRequestParam) {
        if (!umsAdminService.checkLevel(umsAdminRequestParam)) {
            return R.ok("权限不足！");
        }

        if (!umsAdminService.exist(umsAdminRequestParam.getId())) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "管理员不存在！");
        }

        if (!umsAdminService.update(umsAdminRequestParam)) {
            return R.ok(RespCode.FAILURE.getCode(), "更新失败！");
        }

        // 删除Redis中的用户角色、权限、菜单信息
        umsAdminService.delRedisAdminDetailByAdminId(umsAdminRequestParam.getId());
        umsMenuService.delRedisAdminMenuByAdminId(umsAdminRequestParam.getId());
        umsMenuService.delRedisAdminPermissionByAdminId(umsAdminRequestParam.getId());
        umsRoleService.delRedisAdminRolesByAdminId(umsAdminRequestParam.getId());
        umsAdminService.delRedisAdminDataScopeByAdminId(umsAdminRequestParam.getId());

        return R.ok("更新成功！");
    }

    @DeleteMapping()
    @PreAuthorize("@el.check('user:del')")
    public R destroy(@RequestBody Set<Long> ids) {
        if (!umsAdminService.delByIds(ids)) {
            return R.ok("删除失败！");
        }

        for (Long id : ids) {
            umsRoleService.delRedisAdminRolesByAdminId(id);
            umsMenuService.delRedisAdminMenuByAdminId(id);
            umsMenuService.delRedisAdminPermissionByAdminId(id);
            umsAdminService.delRedisAdminDataScopeByAdminId(id);
            umsAdminService.delRedisAdminDetailByAdminId(id);
        }

        return R.ok("删除成功！");
    }

}

