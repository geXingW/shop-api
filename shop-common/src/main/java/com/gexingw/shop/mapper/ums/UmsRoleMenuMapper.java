package com.gexingw.shop.mapper.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.bo.ums.UmsRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@TableName("ums_roles_menus")
public interface UmsRoleMenuMapper extends BaseMapper<UmsRoleMenu> {
    List<UmsRoleMenu> getRoleMenusByRoleIds(List<Long> ids);

    int saveBatch(Long roleId, List<UmsMenu> menus);
}
