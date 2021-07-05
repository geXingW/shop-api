package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_roles_menus")
public class UmsRoleMenu {

    private Long menuId;

    private Long roleId;

}
