package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_admins_roles")
public class UmsAdminRole {

    private Long adminId;

    private Long roleId;
}
