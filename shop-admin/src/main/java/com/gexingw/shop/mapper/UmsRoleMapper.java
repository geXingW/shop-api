package com.gexingw.shop.mapper;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.bo.ums.UmsRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@TableName("ums_roles")
public interface UmsRoleMapper extends BaseMapper<UmsRole> {

    @Select("SELECT * FROM ums_roles WHERE id = #{id}")
    public UmsRole findById(long id);

    public List<UmsRole> getByIds(List<Long> ids);

    List<UmsMenu> getRoleMenus(Long roleId);

    List<UmsDept> getRoleDepts(Long roleId);

//    public IPage<>

//    boolean
}
