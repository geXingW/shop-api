package com.gexingw.shop.modules.oms.service;

import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.modules.oms.dto.OmsCartRequestParam;

import java.util.List;
import java.util.Set;

public interface CartService {
    List<OmsCartItem> getListByMemberId(Long memberId);

    OmsCartItem getByItemIdAndMemberId(String itemId, Long memberId);

    boolean deleteByItemIds(Set<Long> ids);

    boolean delCartItemByItemIds(List<String> itemIds);

    boolean save(OmsCartRequestParam requestParam, PmsProduct product, PmsProductSku productSku);

    OmsCartItem getBySkuIdAndMemberId(Long skuId, Long memberId);

    boolean update(OmsCartItem cartItem);

    OmsCartItem getById(Long id);

    boolean update(OmsCartItem cartItem, PmsProductSku productSku, OmsCartRequestParam requestParam);

    boolean changeCheckStatusByIds(Set<Long> ids);

    boolean clearCartItems();

    boolean deleteByIds(Set<Long> ids);
}
