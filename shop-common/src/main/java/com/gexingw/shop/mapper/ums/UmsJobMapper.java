package com.gexingw.shop.mapper.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsJob;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("ums_jobs")
public interface UmsJobMapper extends BaseMapper<UmsJob> {

}
