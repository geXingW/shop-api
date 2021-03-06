package com.gexingw.shop.modules.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.ums.UmsJobMapper;
import com.gexingw.shop.modules.ums.dto.job.UmsJobRequestParam;
import com.gexingw.shop.modules.ums.dto.job.UmsJobSearchParam;
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
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        return jobId > 0 ? R.ok(jobId, "????????????") : R.failure(RespCode.SAVE_FAILURE);
    }

    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public R update(@RequestBody UmsJobRequestParam jobRequestParam) {
        if (!umsJobService.update(jobRequestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // ?????????????????????????????????????????????
        List<UmsAdmin> admins = umsJobService.getJobAdminsByJobId(jobRequestParam.getId());
        for (UmsAdmin admin : admins) {
            umsJobService.delRedisAdminJobsByAdminId(admin.getId());
        }

        return R.ok(RespCode.ADMIN_JOB_UPDATED);
    }

    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public R delete(@RequestBody List<Long> ids) {
        if (!umsJobService.delete(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        return R.ok(RespCode.ADMIN_JOB_DELETED);
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
            map.put("????????????", umsJob.getName());
            map.put("????????????", umsJob.isEnabled() ? "??????" : "??????");
            map.put("????????????", umsJob.getCreateTime());
            list.add(map);
        }

        FileUtil.downloadExcel(list, response);
    }
}
