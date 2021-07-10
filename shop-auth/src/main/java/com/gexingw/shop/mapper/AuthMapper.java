package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bean.AuthClientDetail;
import com.gexingw.shop.bean.OAuthAccount;
import com.gexingw.shop.bo.ums.UmsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthMapper extends BaseMapper<OAuthAccount> {

    @Select("select um.* from ums_menus um\n" +
            "left join ums_roles_menus urm on um.id = urm.menu_id\n" +
            "left join ums_admins_roles uar on urm.role_id = uar.role_id\n" +
            "left join ums_admins ua on uar.admin_id = ua.id\n" +
            "where ua.id = #{adminId}")
    List<UmsMenu> getAdminPermissions(Long adminId);

    @Select("select * from oauth_client_details where client_id = #{clientId}")
    AuthClientDetail getClientDetailsByClientId(String clientId);
}
