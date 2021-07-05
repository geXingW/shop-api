package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.ums.UmsDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@TableName("ums_depts")
public interface UmsDeptMapper extends BaseMapper<UmsDept> {
    @Select("select * from ums_depts")
    List<UmsDept> getAll();

    @Select("select * from ums_depts where pid = ${pid} order by dept_sort")
    List<UmsDept> getByPid(long pid);

    @Update("update ums_depts set sub_count = sub_count + 1 where id = ${id}")
    int incrSubCount(Long id);
}
