package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsAdminJob;
import com.gexingw.shop.bo.ums.UmsAdminRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@TableName("ums_admins_jobs")
public interface UmsAdminJobMapper extends BaseMapper<UmsAdminJob> {

    @Select("SELECT * FROM ums_admins_roles WHERE admin_id = #{id}")
    public List<UmsAdminRole> getRoleByUserId(Long id);

    public boolean insertAdminJobs(long adminId, List<Long> jobIds);
}
