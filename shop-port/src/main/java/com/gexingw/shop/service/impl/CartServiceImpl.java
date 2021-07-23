package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.dto.oms.OmsCartRequestParam;
import com.gexingw.shop.mapper.oms.OmsCartItemMapper;
import com.gexingw.shop.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper cartItemMapper;

    @Override
    public List<OmsCartItem> getListByMemberId(Long memberId) {
        return cartItemMapper.selectList(new QueryWrapper<OmsCartItem>().eq("member_id", memberId));
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
}
