package com.gexingw.shop.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.pms.PmsProductAttributeMapper;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeService;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Resource
    PmsProductAttributeMapper attributeMapper;

    @Autowired
    PmsProductAttributeValueService attributeValueService;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean update(PmsProductAttributeRequestParam requestParam) {
        PmsProductAttribute productAttribute = attributeMapper.selectById(requestParam.getId());

        boolean nameChanged = !productAttribute.getName().equals(requestParam.getName());

        // 更新属性信息
        BeanUtils.copyProperties(requestParam, productAttribute);
        if (attributeMapper.updateById(productAttribute) <= 0) {
            throw new DBOperationException("商品属性信息更新失败！");
        }

        // 如果属性的名称没有改变，无需更新商品属性值表中的商品名称

        // 更新商品属性值表中的商品属性
        if (nameChanged && !attributeValueService.updateAttributeNameByAttributeId(requestParam.getId(), requestParam.getName())) {
            throw new DBOperationException("商品属性值表商品属性名更新失败！");
        }

        return true;
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

    @Override
    public Map<Long, PmsProductAttribute> getAttrsMapKeyByAttrIdByAttrIds(List<Long> attributeIds) {
        HashMap<Long, PmsProductAttribute> attributeHashMap = new HashMap<>();

        if (!attributeIds.isEmpty()) {
            List<PmsProductAttribute> productAttributes = attributeMapper.selectBatchIds(attributeIds);
            productAttributes.forEach(attribute -> {
                attributeHashMap.put(attribute.getId(), attribute);
            });
        }

        return attributeHashMap;
    }
}
