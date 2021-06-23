package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.ums.UmsAdmin;
import com.gexingw.shop.bean.ums.UmsRole;
import com.gexingw.shop.dto.admin.UmsAdminSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@TableName("ums_admins")
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {
    @Select("SELECT * FROM ums_admins WHERE username = #{username}")
    UmsAdmin findByUserName(String username);

    @Select("SELECT * FROM ums_roles WHERE admin_id = #{id}")
    List<UmsRole> getUserRolesById(Long id);

    //    @Select("select * from ums_admins order by id desc")
    List<UmsAdmin> getList();

    IPage<UmsAdmin> getList2(Page<?> page);

    IPage<UmsAdmin> queryList(Page<?> page, UmsAdminSearchParam requestParams);
}
