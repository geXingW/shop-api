package com.gexingw.shop.service.pms.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.mapper.PmsProductSkuMapper;
import com.gexingw.shop.service.pms.PmsProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PmsProductSkuServiceImpl implements PmsProductSkuService {
    @Autowired
    PmsProductSkuMapper skuMapper;

    @Override
    public boolean save(Long productId, List<PmsProductRequestParam.Sku> skuList) {
        ArrayList<PmsProductSku> productSkuList = new ArrayList<PmsProductSku>();

        for (PmsProductRequestParam.Sku sku : skuList) {
            PmsProductSku pmsProductSku = new PmsProductSku();
            pmsProductSku.setProductId(productId.toString());
            pmsProductSku.setPrice(sku.getPrice());
            pmsProductSku.setStock(sku.getStock());
            pmsProductSku.setLowStock(sku.getLowStock());
            pmsProductSku.setSpData(JSON.toJSONString(sku.getSku()));

            productSkuList.add(pmsProductSku);
        }

        return skuMapper.batchInsert(productSkuList) == skuList.size();
    }

    @Override
    public boolean delProductAttributesByPid(Long productId) {
        return skuMapper.delete(new QueryWrapper<PmsProductSku>().eq("product_id", productId)) >= 0;
    }
}
