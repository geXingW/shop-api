package com.gexingw.shop.modules.ums.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Mapper
public interface AdminUmsAdminMapper extends com.gexingw.shop.mapper.ums.UmsAdminMapper {

    IPage<UmsAdmin> queryList(Page<?> page, UmsAdminSearchParam requestParams);
}
