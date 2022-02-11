package com.gexingw.shop.modules.ums.dto.admin;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class UmsAdminSearchParam extends BaseSearchParam {

    private long deptId = 0L;

    private int enabled = -1;

}
