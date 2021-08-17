package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.service.PmsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    PmsProductMapper productMapper;

    @Override
    public boolean isExist(Long id) {
        return productMapper.selectCount(new QueryWrapper<PmsProduct>().eq("id", id)) > 0;
    }

    @Override
    public PmsProduct getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<PmsProduct> getByIds(List<Long> ids) {
        return productMapper.selectBatchIds(ids);
    }

    @Override
    public boolean decrProductStockById(Long id, Integer quantity) {
        try {
            return productMapper.decrStock(id, quantity) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
