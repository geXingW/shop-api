package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_admins_jobs")
public class UmsAdminJob {

    private Long adminId;

    private Long jobId;
}
