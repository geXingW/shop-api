package com.gexingw.shop.mapper.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsAdminRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@TableName("ums_admins_roles")
public interface UmsAdminRoleMapper extends BaseMapper<UmsAdminRole> {

    @Select("SELECT * FROM ums_admins_roles WHERE admin_id = #{id}")
    public List<UmsAdminRole> getRoleByUserId(Long id);

    public boolean insertAdminRoles(long adminId, List<Long> roleIds);
}
