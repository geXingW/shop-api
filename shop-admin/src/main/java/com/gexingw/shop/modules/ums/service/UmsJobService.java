package com.gexingw.shop.modules.ums.service;

import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.modules.ums.dto.job.UmsJobRequestParam;

import java.util.List;

public interface UmsJobService {

    Long save(UmsJobRequestParam jobRequestParam);

    boolean update(UmsJobRequestParam jobRequestParam);

    boolean delete(List<Long> ids);

    List<UmsJob> getJobsByAdminId(Long adminId);

    List<UmsJob> getRedisAdminJobsByAdminId(Long adminId);

    boolean setRedisAdminJobsByAdminId(Long adminId, List<UmsJob> jobs);

    void delRedisAdminJobsByAdminId(Long adminId);

    List<UmsAdmin> getJobAdminsByJobId(Long jobId);

    List<UmsJob> getJobsByJobIds(List<Long> ids);
}
