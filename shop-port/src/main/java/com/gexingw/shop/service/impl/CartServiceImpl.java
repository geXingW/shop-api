package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.dto.oms.OmsCartRequestParam;
import com.gexingw.shop.mapper.oms.OmsCartItemMapper;
import com.gexingw.shop.service.CartService;
import com.gexingw.shop.vo.Oms.OmsCartItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper cartItemMapper;

    @Override
    public List<OmsCartItemVo> getListByMemberId(Long memberId) {
        List<OmsCartItemVo> omsCartItems = new ArrayList<>();

        List<OmsCartItem> cartItems = cartItemMapper.selectList(new QueryWrapper<OmsCartItem>().eq("member_id", memberId));
//        List<Long> productIds = cartItems.stream().map(OmsCartItem::getItemId).collect(Collectors.toList());

        for (OmsCartItem cartItem: cartItems){
            OmsCartItemVo omsCartItem = new OmsCartItemVo();
            BeanUtils.copyProperties(cartItem, omsCartItem);
            // 改为从数据库查询
            omsCartItem.setLimitQuantity(50);
            omsCartItems.add(omsCartItem);
        }

        return omsCartItems;
    }

    @Override
    public OmsCartItem getByItemIdAndMemberId(Long itemId, Long memberId) {
        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<OmsCartItem>().eq("member_id", memberId)
                .eq("item_id", itemId);
        return cartItemMapper.selectOne(queryWrapper);
    }

    @Override
    public Long save(OmsCartRequestParam requestParam) {
        OmsCartItem cartItem = new OmsCartItem();
        BeanUtils.copyProperties(requestParam, cartItem);
        cartItem.setItemPrice(requestParam.getItemPrice());

        if (cartItemMapper.insert(cartItem) <= 0) {
            return null;
        }

        return cartItem.getId();
    }

    @Override
    public boolean update(OmsCartRequestParam requestParam) {
        OmsCartItem cartItem = new OmsCartItem();
        BeanUtils.copyProperties(requestParam, cartItem);
        cartItem.setItemPrice(requestParam.getItemPrice());

        return cartItemMapper.updateById(cartItem) > 0;
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        return cartItemMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean deleteByItemIds(Set<Long> ids) {
        return cartItemMapper.delete(new QueryWrapper<OmsCartItem>().in("item_id", ids)) > 0;
    }
}
