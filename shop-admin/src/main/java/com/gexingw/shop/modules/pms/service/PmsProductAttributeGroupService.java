package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeGroupRequestParam;

import java.util.List;
import java.util.Set;

public interface PmsProductAttributeGroupService {
    IPage<PmsProductAttributeGroup> search(QueryWrapper<PmsProductAttributeGroup> queryWrapper, Page<PmsProductAttributeGroup> page);

    Long save(PmsProductAttributeGroupRequestParam requestParam);

    Integer getCntById(Long id);

    boolean update(PmsProductAttributeGroupRequestParam requestParam);

    boolean deleteByBatchIds(Set<Long> ids);

    List<Long> getAttachAttributeIdsByGroupId(Long groupId);

    boolean attachAttributeToGroup(Long groupId, List<Long> attributeIds);

    boolean detachAttributeToGroup(Long groupId, Set<Long> attributeIds);

    PmsProductAttributeGroup findById(Long id);

    List<PmsProductAttributeGroup> getAttributeGroupsByCategoryId(Long categoryId);

    List<Long> getAttachAttributeIdsByGroupIds(List<Long> attributeGroupIds);
}
