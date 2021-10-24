package com.gexingw.shop.modules.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.modules.ums.dto.job.UmsJobRequestParam;
import com.gexingw.shop.modules.ums.dto.job.UmsJobSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.ums.UmsJobMapper;
import com.gexingw.shop.modules.ums.service.UmsJobService;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("job")
public class JobController {

    @Autowired
    UmsJobMapper umsJobMapper;

    @Autowired
    UmsJobService umsJobService;

    @GetMapping
    @PreAuthorize("@el.check('job:list')")
    public R index(UmsJobSearchParam jobRequestParams) {
        QueryWrapper<UmsJob> queryWrapper = new QueryWrapper<>();

        // enabled
        if (jobRequestParams.getEnabled() != null) {
            queryWrapper.eq("enabled", jobRequestParams.getEnabled());
        }
        Page<UmsJob> roleList = umsJobMapper.selectPage(new Page<UmsJob>(jobRequestParams.getPage(), jobRequestParams.getSize()), queryWrapper);

        return R.ok(PageUtil.format(roleList));
    }

    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public R save(@RequestBody UmsJobRequestParam jobRequestParam) {
        Long jobId = umsJobService.save(jobRequestParam);
        if (umsJobService.save(jobRequestParam) <= 0) {
            return R.ok("保存失败！");
        }

        return jobId > 0 ? R.ok(jobId, "已保存！") : R.ok("保存失败！");
    }

    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public R update(@RequestBody UmsJobRequestParam jobRequestParam) {
        if (!umsJobService.update(jobRequestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE, "更新失败！");
        }

        // 清除与该角色相关联的管理员缓存
        List<UmsAdmin> admins = umsJobService.getJobAdminsByJobId(jobRequestParam.getId());
        for (UmsAdmin admin : admins) {
            umsJobService.delRedisAdminJobsByAdminId(admin.getId());
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public R delete(@RequestBody List<Long> ids) {
        if (!umsJobService.delete(ids)) {
            return R.ok(RespCode.DELETE_FAILURE, "删除失败！");
        }

        return R.ok("已删除！");
    }

    @GetMapping("download")
    @PreAuthorize("@el.check('job:list')")
    public void download(UmsJobSearchParam requestParam, HttpServletResponse response) throws IOException {
        QueryWrapper<UmsJob> queryWrapper = new QueryWrapper<>();

        // enabled
        if (requestParam.getEnabled() != null) {
            queryWrapper.eq("enabled", requestParam.getEnabled());
        }

        Page<UmsJob> roleList = umsJobMapper.selectPage(new Page<UmsJob>(requestParam.getPage(), requestParam.getSize()), queryWrapper);
        List<UmsJob> records = roleList.getRecords();

        List<Map<String, Object>> list = new ArrayList();
        for (UmsJob umsJob : records) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("岗位名称", umsJob.getName());
            map.put("岗位状态", umsJob.isEnabled() ? "启用" : "停用");
            map.put("创建日期", umsJob.getCreateTime());
            list.add(map);
        }

        FileUtil.downloadExcel(list, response);
    }
}
