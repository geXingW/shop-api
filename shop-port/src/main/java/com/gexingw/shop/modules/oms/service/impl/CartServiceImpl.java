package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.constant.OmsCartConstant;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.oms.OmsCartItemMapper;
import com.gexingw.shop.modules.oms.dto.OmsCartRequestParam;
import com.gexingw.shop.modules.oms.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper cartItemMapper;

    @Override
    public List<OmsCartItem> getListByMemberId(Long memberId) {
        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId);

        return cartItemMapper.selectList(queryWrapper);
    }

    @Override
    public OmsCartItem getByItemIdAndMemberId(String itemId, Long memberId) {
        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<OmsCartItem>().eq("member_id", memberId)
                .eq("item_id", itemId);
        return cartItemMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean save(OmsCartRequestParam requestParam, PmsProduct product, PmsProductSku productSku) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setMemberId(requestParam.getMemberId());
        cartItem.setItemId(product.getId());
        cartItem.setItemQuantity(requestParam.getProductCnt());
        cartItem.setItemPic(product.getPic());
        cartItem.setItemTitle(product.getTitle());
        cartItem.setItemSubTitle(product.getSubTitle());
        cartItem.setItemPrice(product.getSalePrice());
        if (productSku != null) {
            cartItem.setItemPrice(productSku.getPrice());
            cartItem.setSkuId(productSku.getId());
            cartItem.setSkuData(productSku.getSpData());
        }

        return cartItemMapper.insert(cartItem) > 0;
    }

    @Override
    public OmsCartItem getBySkuIdAndMemberId(Long skuId, Long memberId) {
        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId).eq("sku_id", skuId);

        return cartItemMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean update(OmsCartItem cartItem) {
        return cartItemMapper.updateById(cartItem) > 0;
    }

    @Override
    public OmsCartItem getById(Long id) {
        return cartItemMapper.selectById(id);
    }

    @Override
    public boolean update(OmsCartItem cartItem, PmsProductSku productSku, OmsCartRequestParam requestParam) {
        // ?????????????????????
        if (productSku != null && !Objects.equals(cartItem.getSkuId(), requestParam.getSkuId())) {
            cartItem.setSkuId(productSku.getId());
            cartItem.setSkuData(productSku.getSpData());
        }

        // ????????????
        cartItem.setItemQuantity(requestParam.getProductCnt());

        // ????????????
        cartItem.setChecked(OmsCartConstant.CART_ITEM_CHECKED);

        return cartItemMapper.updateById(cartItem) >= 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeCheckStatusByIds(Set<Long> ids) {
        OmsCartItem cartItem = new OmsCartItem();

        // ??????id??????????????????????????????????????????
        if (ids.isEmpty()) {
            cartItem.setChecked(0);
            if (cartItemMapper.update(cartItem, new QueryWrapper<OmsCartItem>()) <= 0) {
                throw new DBOperationException("????????????????????????");
            }

            return true;
        }

        // ?????????id?????????????????????
        cartItem.setChecked(1);
        if (cartItemMapper.update(cartItem, new QueryWrapper<OmsCartItem>().in("id", ids)) < 0) {
            throw new DBOperationException("????????????????????????");
        }

        // ????????????id??????????????????
        cartItem.setChecked(0);
        if (cartItemMapper.update(cartItem, new QueryWrapper<OmsCartItem>().notIn("id", ids)) < 0) {
            throw new DBOperationException("????????????????????????");
        }

        return true;
    }

    @Override
    public boolean clearCartItems() {
        return cartItemMapper.delete(new QueryWrapper<>()) >= 0;
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        return cartItemMapper.delete(new QueryWrapper<OmsCartItem>().in("id", ids)) >= 0;
    }

    @Override
    public List<OmsCartItem> getByIds(List<Long> ids) {
        return cartItemMapper.selectBatchIds(ids);
    }

    @Override
    public boolean delCartItemsByIds(List<Long> ids) {
        return cartItemMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<OmsCartItem> search(QueryWrapper<OmsCartItem> queryWrapper) {
        return cartItemMapper.selectList(queryWrapper);
    }

}
