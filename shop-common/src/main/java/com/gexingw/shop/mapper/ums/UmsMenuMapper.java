package com.gexingw.shop.mapper.ums;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@TableName("ums_menus")
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {
    List<UmsMenu> getMenusByIds(List<Long> ids);

    @Select("SELECT * FROM ums_roles_menu WHERE id = #{id}")
    UmsMenu findById(long id);

    List<UmsMenu> getByPidAndIds(long pid, List<Long> ids);

    List<UmsMenu> getByPid(long pid);

    @Select("select * from ums_menus")
    IPage<UmsMenu> testPager(Page<?> page);

    @Select("select * from ums_menus")
    IPage<UmsMenu> testPagerUtil(Page<?> page);
}
