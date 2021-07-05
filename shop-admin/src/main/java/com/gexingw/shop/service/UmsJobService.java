package com.gexingw.shop.service;

import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.dto.job.UmsJobRequestParam;

import java.util.List;

public interface UmsJobService {

    Long save(UmsJobRequestParam jobRequestParam);

    boolean update(UmsJobRequestParam jobRequestParam);

    boolean delete(List<Long> ids);

    List<UmsJob> getJobsByAdminId(Long adminId);

    public List<UmsJob> getRedisAdminJobsByAdminId(Long adminId);

    public boolean setRedisAdminJobsByAdminId(Long adminId, List<UmsJob> jobs);

    public void delRedisAdminJobsByAdminId(Long adminId);

    List<UmsAdmin> getJobAdminsByJobId(Long jobId);
}
