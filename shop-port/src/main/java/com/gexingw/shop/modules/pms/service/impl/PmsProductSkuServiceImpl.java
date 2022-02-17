package com.gexingw.shop.modules.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.mapper.pms.PmsProductSkuMapper;
import com.gexingw.shop.modules.pms.dto.product.PmsProductPriceRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsProductSkuServiceImpl implements PmsProductSkuService {
    @Autowired
    PmsProductSkuMapper productSkuMapper;

    @Override
    public PmsProductSku getById(Long id) {
        return productSkuMapper.selectById(id);
    }

    @Override
    public PmsProductSku getByProductIdAndSkuData(String productId, List<PmsProductPriceRequestParam.Option> spData) {
        String spDataJson = JSON.toJSONString(spData);

        QueryWrapper<PmsProductSku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId).eq("sp_data", spDataJson);

        return productSkuMapper.selectOne(queryWrapper);
    }
}
