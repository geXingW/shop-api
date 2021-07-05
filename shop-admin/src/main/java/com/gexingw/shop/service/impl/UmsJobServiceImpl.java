package com.gexingw.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsAdminJob;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.constant.AdminConstant;
import com.gexingw.shop.dto.job.UmsJobRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.exception.IllegalOperationException;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.UmsAdminJobMapper;
import com.gexingw.shop.mapper.UmsAdminMapper;
import com.gexingw.shop.mapper.UmsJobMapper;
import com.gexingw.shop.service.UmsJobService;
import com.gexingw.shop.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsJobServiceImpl implements UmsJobService {
    @Autowired
    UmsJobMapper umsJobMapper;

    @Autowired
    UmsAdminJobMapper umsAdminJobMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsAdminMapper umsAdminMapper;

    @Override
    public Long save(UmsJobRequestParam jobRequestParam) {
        UmsJob umsJob = new UmsJob();
        BeanUtils.copyProperties(jobRequestParam, umsJob);

        if (umsJobMapper.insert(umsJob) <= 0) {
            throw new RuntimeException("保存失败！");
        }

        return umsJob.getId();
    }

    @Override
    public boolean update(UmsJobRequestParam jobRequestParam) {
        UmsJob umsJob = umsJobMapper.selectById(jobRequestParam.getId());
        if (umsJob == null) {
            throw new ResourceNotExistException("该岗位不存在！");
        }

        BeanUtils.copyProperties(jobRequestParam, umsJob);
        if (umsJobMapper.updateById(umsJob) <= 0) {
            throw new DBOperationException("更新失败！");
        }

        return true;
    }

    @Override
    public boolean delete(List<Long> ids) {
        // 检查当前岗位是否有人占用
        if (umsAdminJobMapper.selectCount(new QueryWrapper<UmsAdminJob>().in("job_id", ids)) > 0) {
            throw new IllegalOperationException("当前岗位有人使用，暂不能删除！");
        }

        if (umsJobMapper.deleteBatchIds(ids) <= 0) {
            throw new DBOperationException("删除失败！");
        }

        return false;
    }

    @Override
    public List<UmsJob> getJobsByAdminId(Long adminId) {
        // 查询Redis是否存在
        List<UmsJob> jobs = new ArrayList<>();
        jobs = getRedisAdminJobsByAdminId(adminId);
        if (jobs != null) {
            return jobs;
        }

        // 查找所有job id
        List<UmsAdminJob> adminJobs = umsAdminJobMapper.selectList(new QueryWrapper<UmsAdminJob>().eq("admin_id", adminId));
        List<Long> jobIds = adminJobs.stream().map(UmsAdminJob::getJobId).collect(Collectors.toList());

        jobs = umsJobMapper.selectBatchIds(jobIds);

        // 将查询结果写入Redis
        setRedisAdminJobsByAdminId(adminId, jobs);

        return jobs;
    }

    public boolean setRedisAdminJobsByAdminId(Long adminId, List<UmsJob> jobs) {
        for (UmsJob umsJob : jobs) {
            redisUtil.lSet(String.format(AdminConstant.REDIS_ADMIN_JOBS_FORMAT, adminId), umsJob, -1);
        }

        return true;
    }

    public List<UmsJob> getRedisAdminJobsByAdminId(Long adminId) {
        String redisKey = String.format(AdminConstant.REDIS_ADMIN_JOBS_FORMAT, adminId);
        if (!redisUtil.hasKey(redisKey)) {
            return null;
        }

        List<Object> redisObjs = redisUtil.lGet(redisKey, 0, -1);
        TypeReference<List<UmsJob>> typeReference = new TypeReference<List<UmsJob>>() {
        };

        return JSON.parseObject(JSON.toJSONString(redisObjs), typeReference);
    }

    public void delRedisAdminJobsByAdminId(Long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_JOBS_FORMAT, adminId));
    }

    @Override
    public List<UmsAdmin> getJobAdminsByJobId(Long jobId) {
        List<UmsAdminJob> adminJobs = umsAdminJobMapper.selectList(new QueryWrapper<UmsAdminJob>().eq("job_id", jobId));
        List<Long> adminIds = adminJobs.stream().map(UmsAdminJob::getAdminId).collect(Collectors.toList());
        return umsAdminMapper.selectBatchIds(adminIds);
    }
}
