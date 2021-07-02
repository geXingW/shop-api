package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bean.pms.PmsProduct;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.mapper.PmsProductMapper;
import com.gexingw.shop.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    PmsProductMapper productMapper;

    @Override
    public IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> page) {
        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long save(PmsProductRequestParam requestParam) {
        PmsProduct product = new PmsProduct();

        BeanUtils.copyProperties(requestParam, product);

        if (productMapper.insert(product) <= 0) {
            return null;
        }

        return product.getId();
    }

    @Override
    public boolean update(PmsProductRequestParam requestParam) {
        PmsProduct product = productMapper.selectById(requestParam.getId());
        if (product == null) {
            return false;
        }

        BeanUtils.copyProperties(requestParam, product);

        return productMapper.updateById(product) <= 0;
    }

    @Override
    public PmsProduct getById(Long id) {
        return productMapper.selectById(id);
    }
}
