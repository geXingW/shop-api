package com.gexingw.shop.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.dto.product.PmsProductAttributeGroupRequestParam;
import com.gexingw.shop.mapper.pms.PmsProductAttributeGroupMapper;
import com.gexingw.shop.service.pms.PmsProductAttributeGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class PmsProductAttributeGroupServiceImpl implements PmsProductAttributeGroupService {

    @Resource
    PmsProductAttributeGroupMapper groupMapper;

    @Override
    public IPage<PmsProductAttributeGroup> search(QueryWrapper<PmsProductAttributeGroup> queryWrapper, Page<PmsProductAttributeGroup> page) {
        return groupMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long save(PmsProductAttributeGroupRequestParam requestParam) {
        PmsProductAttributeGroup attributeGroup = new PmsProductAttributeGroup();
        BeanUtils.copyProperties(requestParam, attributeGroup);

        if (groupMapper.insert(attributeGroup) <= 0) {
            return null;
        }

        return attributeGroup.getId();
    }

    @Override
    public Integer getCntById(Long id) {
        return groupMapper.selectCount(new QueryWrapper<PmsProductAttributeGroup>().eq("id", id));
    }

    @Override
    public boolean update(PmsProductAttributeGroupRequestParam requestParam) {
        PmsProductAttributeGroup attributeGroup = new PmsProductAttributeGroup();
        BeanUtils.copyProperties(requestParam, attributeGroup);

        return groupMapper.updateById(attributeGroup) > 0;
    }

    @Override
    public boolean deleteByBatchIds(Set<Long> ids) {
        return groupMapper.deleteBatchIds(ids) > 0;
    }
}
