package com.gexingw.shop.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.sys.SysCity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysCityMapper extends BaseMapper<SysCity> {

    @Select("select * from sys_city where name = #{cityName}")
    SysCity findByName(String cityName);

    @Select("select * from sys_city where name = #{cityName} and code = #{code}")
    SysCity findByNameAndCode(String cityName, Integer code);

    @Select("select * from sys_city where name = #{cityName} and parent_code = #{parentCode}")
    SysCity findByNameAndParentCode(String city, Integer parentCode);
}
