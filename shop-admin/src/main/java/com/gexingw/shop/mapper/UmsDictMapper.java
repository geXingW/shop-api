package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bean.ums.UmsDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@TableName("ums_dict")
public interface UmsDictMapper extends BaseMapper<UmsDict> {
    @Select("select * from ums_dicts where name = #{name}")
    UmsDict findByName(String name);
}
