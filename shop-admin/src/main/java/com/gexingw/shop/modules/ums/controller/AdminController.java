package com.gexingw.shop.modules.ums.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.modules.sys.service.CommonService;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminRequestParam;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.ums.mapper.AdminUmsAdminMapper;
import com.gexingw.shop.modules.ums.service.UmsAdminService;
import com.gexingw.shop.modules.ums.service.UmsDeptService;
import com.gexingw.shop.modules.ums.service.UmsMenuService;
import com.gexingw.shop.modules.ums.service.UmsRoleService;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private AdminUmsAdminMapper adminUmsAdminMapper;

    @Autowired
    UmsDeptService umsDeptService;

    @Autowired
    UmsMenuService umsMenuService;

    @Autowired
    UmsRoleService umsRoleService;

    @Autowired
    CommonService commonService;

    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public R index(UmsAdminSearchParam requestParams) {
        IPage<UmsAdmin> page = adminUmsAdminMapper.queryList(new Page<>(requestParams.getPage(), requestParams.getSize()), requestParams);
        return R.ok(PageUtil.format(page));
    }

    @GetMapping("download")
    @PreAuthorize("@el.check('user:list')")
    public void download(UmsAdminSearchParam searchParams, HttpServletResponse response) throws IOException {
        IPage<UmsAdmin> page = adminUmsAdminMapper.queryList(new Page<>(searchParams.getPage(), searchParams.getSize()), searchParams);
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

    /**
     * 上传接口
     *
     * @return
     */
    @PostMapping("/upload-avatar")
    public R upload(@RequestParam MultipartFile file, @RequestParam String uploadType, @RequestParam Long uploadId) {
        // 上传文件获取服务器文件路径
        File uploadedFile = commonService.upload(file, uploadType);
        if (uploadedFile == null) {
            return R.ok("上传失败！");
        }

        // 删除旧文件
        if (!commonService.detachOldFile(uploadId, uploadType)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "旧图片删除失败！");
        }

        // 资源与新图片绑定
        SysUpload upload = commonService.attachUploadFile(uploadId, uploadType, uploadedFile);
        if (upload == null) {
            return R.ok(RespCode.UPLOAD_FAILURE.getCode(), "上传失败！");
        }

        return R.ok("上传成功！");
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
        umsDeptService.delRedisAdminDeptByAdminId(umsAdminRequestParam.getId());

        return R.ok("更新成功！");
    }

    @PutMapping("center")
    public R updateCenter(@RequestBody UmsAdminRequestParam requestParam) {
        try {
            if (!umsAdminService.updateCenter(requestParam)) {
                return R.ok(RespCode.FAILURE.getCode(), "更新失败！");
            }
        } catch (Exception e) {
            return R.ok(RespCode.FAILURE.getCode(), e.getMessage());
        }

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
            umsDeptService.delRedisAdminDeptByAdminId(id);
        }

        return R.ok("删除成功！");
    }

}

