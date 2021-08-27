package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.dto.product.PmsProductAttributeRequestParam;

import java.util.Set;

public interface PmsProductAttributeService {

    IPage<PmsProductAttribute> searchList(QueryWrapper<PmsProductAttribute> queryWrapper, Page<PmsProductAttribute> page);

    Long save(PmsProductAttributeRequestParam requestParam);

    boolean update(PmsProductAttributeRequestParam requestParam);

    PmsProductAttribute findById(Long id);

    boolean deleteByIds(Set<Long> ids);
}