package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.dto.product.PmsProductAttributeRequestParam;
import com.gexingw.shop.mapper.pms.PmsProductAttributeMapper;
import com.gexingw.shop.service.PmsProductAttributeService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Resource
    PmsProductAttributeMapper attributeMapper;

    @Override
    public IPage<PmsProductAttribute> searchList(QueryWrapper<PmsProductAttribute> queryWrapper, Page<PmsProductAttribute> page) {
        return attributeMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long save(PmsProductAttributeRequestParam requestParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(requestParam, productAttribute);

        return attributeMapper.insert(productAttribute) > 0 ? productAttribute.getId() : null;
    }

    @Override
    public boolean update(PmsProductAttributeRequestParam requestParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(requestParam, productAttribute);

        return attributeMapper.updateById(productAttribute) > 0;
    }

    @Override
    public PmsProductAttribute findById(Long id) {
        return attributeMapper.selectById(id);
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        return attributeMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<PmsProductAttribute> getAttributesByTypeAndIds(Integer type, List<Long> ids) {
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<PmsProductAttribute>().eq("type", type).in("id", ids);
        return attributeMapper.selectList(queryWrapper);
    }
}
