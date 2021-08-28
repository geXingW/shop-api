package com.gexingw.shop.service.pms;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.dto.product.PmsProductAttributeGroupRequestParam;

import java.util.Set;

public interface PmsProductAttributeGroupService {
    IPage<PmsProductAttributeGroup> search(QueryWrapper<PmsProductAttributeGroup> queryWrapper, Page<PmsProductAttributeGroup> page);

    Long save(PmsProductAttributeGroupRequestParam requestParam);

    Integer getCntById(Long id);

    boolean update(PmsProductAttributeGroupRequestParam requestParam);

    boolean deleteByBatchIds(Set<Long> ids);
}