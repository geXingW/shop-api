package com.gexingw.shop.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public PmsProduct getById(String id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<PmsProduct> getByIds(List<String> ids) {
        return productMapper.selectBatchIds(ids);
    }

    @Override
    public IPage<PmsProduct> search(PmsProductSearchRequestParam requestParam) {
        // 分页
        Page<PmsProduct> page = new Page<>(requestParam.getPage(), requestParam.getSize());

        // 条件查询
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        String keywords = requestParam.getKeywords();

        // 没有关键字
        if (keywords == null) {
            return productMapper.selectPage(page, queryWrapper);
        }

        // 根据关键字，匹配查询
        queryWrapper.and(
                q -> q.like("title", keywords).or()
                        .like("sub_title", keywords)
        );

        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean lockStockByProductId(String id, Integer quantity) {
        return productMapper.lockStock(id, quantity) > 0;
    }

    @Override
    public boolean addProductSaleCount(ArrayList<OmsCartItem> cartItems) {
        for (OmsCartItem cartItem : cartItems) {
            if (!productMapper.addSaleCount(cartItem.getItemId(), cartItem.getItemQuantity())) {
                return false;
            }
        }

        return true;
    }
}
