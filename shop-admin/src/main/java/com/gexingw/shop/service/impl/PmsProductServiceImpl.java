package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.service.PmsProductService;
import com.gexingw.shop.service.pms.PmsProductAttributeValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    PmsProductMapper productMapper;

    @Autowired
    PmsProductAttributeValueService attributeValueService;

    @Override
    public IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> page) {
        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(PmsProductRequestParam requestParam) {
        PmsProduct product = new PmsProduct();

        BeanUtils.copyProperties(requestParam, product);
        product.setAlbumPics(requestParam.getPics());   // 相册

        if (productMapper.insert(product) <= 0) {
            return null;
        }

        Long productId = product.getId();   // 新的商品ID

        if(!attributeValueService.save(productId, requestParam.getAttributeList())){
            throw new RuntimeException("商品基本属性保存失败!");
        }

        return productId;
    }

    @Override
    public boolean update(PmsProductRequestParam requestParam) {
        PmsProduct product = productMapper.selectById(requestParam.getId());
        if (product == null) {
            return false;
        }

        BeanUtils.copyProperties(requestParam, product);

        return productMapper.updateById(product) > 0;
    }

    @Override
    public PmsProduct getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public boolean delete(Set<Long> ids) {
        return productMapper.deleteBatchIds(ids) > 0;
    }
}
