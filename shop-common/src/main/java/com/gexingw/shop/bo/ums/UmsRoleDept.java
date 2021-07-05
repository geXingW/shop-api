package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("ums_roles_depts")
@AllArgsConstructor
public class UmsRoleDept {
    private Long roleId;

    private Long deptId;
}
