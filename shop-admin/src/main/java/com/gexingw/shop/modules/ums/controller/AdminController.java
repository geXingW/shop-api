package com.gexingw.shop.modules.ums.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.sys.service.CommonService;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminRequestParam;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminSearchParam;
import com.gexingw.shop.modules.ums.mapper.AdminUmsAdminMapper;
import com.gexingw.shop.modules.ums.service.*;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    UmsJobService umsJobService;

    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public R index(UmsAdminSearchParam requestParams) {
        IPage<UmsAdmin> page = new Page<>(requestParams.getPage(), requestParams.getSize());
        IPage<UmsAdmin> umsAdminsPage = umsAdminService.queryList(page, requestParams);

        List<UmsAdmin> umsAdminsRecords = umsAdminsPage.getRecords();

        Map<String, Object> result = PageUtil.format(umsAdminsPage);

        // 获取所有admin所属的部门信息
        List<Long> umsAdminDeptIds = umsAdminsRecords.stream().map(UmsAdmin::getDeptId).collect(Collectors.toList());
        if (umsAdminDeptIds.size() > 0) {
            HashMap<Long, UmsDept> umsDeptHashMap = new HashMap<>();
            umsDeptService.getDeptsByDeptId(umsAdminDeptIds).forEach(umsDept -> umsDeptHashMap.put(umsDept.getId(), umsDept));

            for (UmsAdmin umsadmin : umsAdminsRecords) {
                umsadmin.setDept(umsDeptHashMap.get(umsadmin.getDeptId()));
            }
        }

        // 获取所有admin的岗位信息
        List<Long> umsAdminJobIds = umsAdminsRecords.stream().map(UmsAdmin::getJobId).collect(Collectors.toList());
        if (umsAdminJobIds.size() > 0) {
            HashMap<Long, UmsJob> umsJobHashMap = new HashMap<>(umsAdminJobIds.size());

            umsJobService.getJobsByJobIds(umsAdminJobIds).forEach(umsJob -> umsJobHashMap.put(umsJob.getId(), umsJob));

            for (UmsAdmin umsadmin : umsAdminsRecords) {
                umsadmin.setJob(umsJobHashMap.get(umsadmin.getJobId()));
            }
        }

        result.put("records", umsAdminsRecords);

        return R.ok(result);
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
    public R add(@RequestBody @Validated UmsAdminRequestParam umsAdminRequestParam) {
        if (!umsAdminService.checkLevel(umsAdminRequestParam)) {
            return R.failure(RespCode.OPERATION_DENY);
        }

        return umsAdminService.save(umsAdminRequestParam) > 0 ? R.ok(RespCode.ADMIN_CREATED) : R.failure(RespCode.SAVE_FAILURE);
    }

    /**
     * 上传接口
     *
     * @return
     */
    @PostMapping("/upload-avatar")
    public R upload(@RequestParam MultipartFile file,
                    @RequestParam String uploadType,
                    @RequestParam Long uploadId,
                    @RequestParam String uploadModule) {
        // 上传文件获取服务器文件路径
        File uploadedFile = commonService.upload(file, uploadType, uploadModule);
        if (uploadedFile == null) {
            return R.failure(RespCode.UPLOAD_FAILURE);
        }

        // 删除旧文件
        if (!commonService.detachOldFile(uploadId, uploadType)) {
            return R.failure(RespCode.FILE_DELETE_FAILURE, "旧图片删除失败！");
        }

        // 资源与新图片绑定
        SysUpload upload = commonService.attachUploadFile(uploadId, uploadType, uploadedFile);
        if (upload == null) {
            return R.failure(RespCode.UPLOAD_FAILURE, "上传失败！");
        }

        return R.ok("上传成功！");
    }

    @PutMapping()
    @PreAuthorize("@el.check('user:edit')")
    public R update(@RequestBody UmsAdminRequestParam umsAdminRequestParam) {
        if (!umsAdminService.checkLevel(umsAdminRequestParam)) {
            return R.failure(RespCode.OPERATION_DENY, "权限不足！");
        }

        if (!umsAdminService.exist(umsAdminRequestParam.getId())) {
            return R.failure(RespCode.ADMIN_NOT_EXIST);
        }

        if (!umsAdminService.update(umsAdminRequestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // 删除Redis中的用户角色、权限、菜单信息
        umsAdminService.delRedisAdminDetailByAdminId(umsAdminRequestParam.getId());
        umsMenuService.delRedisAdminMenuByAdminId(umsAdminRequestParam.getId());
        umsMenuService.delRedisAdminPermissionByAdminId(umsAdminRequestParam.getId());
        umsRoleService.delRedisAdminRolesByAdminId(umsAdminRequestParam.getId());
        umsAdminService.delRedisAdminDataScopeByAdminId(umsAdminRequestParam.getId());
        umsDeptService.delRedisAdminDeptByAdminId(umsAdminRequestParam.getId());

        return R.ok(RespCode.ADMIN_UPDATED);
    }

    @PutMapping("center")
    public R updateCenter(@RequestBody UmsAdminRequestParam requestParam) {
        try {
            if (!umsAdminService.updateCenter(requestParam)) {
                return R.failure(RespCode.UPDATE_FAILURE);
            }
        } catch (Exception e) {
            return R.failure(RespCode.UPDATE_FAILURE, e.getMessage());
        }

        return R.ok(RespCode.ADMIN_UPDATED);
    }

    @DeleteMapping()
    @PreAuthorize("@el.check('user:del')")
    public R destroy(@RequestBody Set<Long> ids) {
        if (!umsAdminService.delByIds(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        for (Long id : ids) {
            umsRoleService.delRedisAdminRolesByAdminId(id);
            umsMenuService.delRedisAdminMenuByAdminId(id);
            umsMenuService.delRedisAdminPermissionByAdminId(id);
            umsAdminService.delRedisAdminDataScopeByAdminId(id);
            umsAdminService.delRedisAdminDetailByAdminId(id);
            umsDeptService.delRedisAdminDeptByAdminId(id);
        }

        return R.ok(RespCode.ADMIN_DELETED);
    }

}

