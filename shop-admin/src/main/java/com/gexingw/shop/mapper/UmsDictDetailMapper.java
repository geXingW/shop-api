package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsDictDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@TableName("ums_dict_details")
public interface UmsDictDetailMapper extends BaseMapper<UmsDictDetail> {

//    @Select("select * from ums_dict_details where dict_id = #{dictId}")
    List<UmsDictDetail> getByDictId(long dictId);
}
