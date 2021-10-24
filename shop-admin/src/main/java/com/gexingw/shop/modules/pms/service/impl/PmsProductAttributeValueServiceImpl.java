package com.gexingw.shop.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.pms.PmsProductAttributeValue;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;
import com.gexingw.shop.mapper.pms.PmsProductAttributeValueMapper;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PmsProductAttributeValueServiceImpl implements PmsProductAttributeValueService {

    @Resource
    PmsProductAttributeValueMapper valueMapper;

    @Override
    public boolean save(Long productId, List<PmsProductRequestParam.Attribute> attributeList) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (PmsProductRequestParam.Attribute attribute : attributeList) {
            String attributeValue = attribute.getValue();
            if (attributeValue == null || attributeValue.isEmpty()) { // 属性值为空的属性无需存储，亦不进行展示
                continue;
            }

            HashMap<String, Object> item = new HashMap<>();
            item.put("productId", productId);
            item.put("attributeId", attribute.getId());
            item.put("attributeValue", attributeValue);

            list.add(item);
        }

        return valueMapper.batchSave(list) == attributeList.size();
    }

    @Override
    public List<PmsProductAttributeValue> getAttributeValuesByPid(Long id) {
        return valueMapper.selectList(new QueryWrapper<PmsProductAttributeValue>().eq("product_id", id));
    }

    @Override
    public boolean delProductAttributesByPid(Long productId) {
        return valueMapper.delete(new QueryWrapper<PmsProductAttributeValue>().eq("product_id", productId)) >= 0;
    }

    @Override
    public boolean updateAttributeNameByAttributeId(Long attributeId, String attributeName) {
        return valueMapper.updateAttributeNameByAttributeId(attributeId, attributeName) > 0;
    }

    @Override
    public boolean batchDelProductAttributesByProductIds(Set<Long> ids) {
        return valueMapper.delete(new QueryWrapper<PmsProductAttributeValue>().in("product_id", ids)) >= 0;
    }

}
