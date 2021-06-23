package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bean.ums.UmsDept;
import com.gexingw.shop.bean.ums.UmsRoleDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@TableName("ums_roles_depts")
public interface UmsRoleDeptMapper extends BaseMapper<UmsRoleDept> {

    int saveBatch(Long roleId, List<UmsDept> depts);
}
