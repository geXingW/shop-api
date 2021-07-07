package com.gexingw.shop.mapper;

import com.gexingw.shop.bo.ums.UmsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthPermissionMapper {
    @Select("select um.* from ums_menus um\n" +
            "left join ums_roles_menus urm on um.id = urm.menu_id\n" +
            "left join ums_admins_roles uar on urm.role_id = uar.role_id\n" +
            "left join ums_admins ua on uar.admin_id = ua.id\n" +
            "where ua.id = #{adminId}")
    List<UmsMenu> getAuthPermission(Long adminId);
}
