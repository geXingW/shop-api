package com.gexingw.shop.service.pms.impl;

import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.mapper.pms.PmsProductAttributeValueMapper;
import com.gexingw.shop.service.pms.PmsProductAttributeValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PmsProductAttributeValueServiceImpl implements PmsProductAttributeValueService {

    @Resource
    PmsProductAttributeValueMapper valueMapper;

    @Override
    public boolean save(Long productId, List<PmsProductRequestParam.Attribute> attributeList) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (PmsProductRequestParam.Attribute attribute : attributeList) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("productId", productId);
            item.put("attributeId", attribute.getId());
            item.put("attributeValue", attribute.getValue());

            list.add(item);
        }

        return valueMapper.batchSave(list) > 0;
    }

    private boolean delProductAttributeValues(Long productId) {
        return valueMapper.deleteByProductId(productId) > 0;
    }

}
