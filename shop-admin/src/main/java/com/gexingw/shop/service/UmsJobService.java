package com.gexingw.shop.service;

import com.gexingw.shop.bean.ums.UmsAdmin;
import com.gexingw.shop.bean.ums.UmsJob;
import com.gexingw.shop.dto.job.UmsJobRequestParam;
import org.springframework.stereotype.Service;

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
