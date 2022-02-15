package com.gexingw.shop.modules.oms.service;

import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.modules.oms.dto.OmsCartRequestParam;
import com.gexingw.shop.modules.oms.vo.OmsCartItemVo;

import java.util.List;
import java.util.Set;

public interface CartService {
    List<OmsCartItemVo> getListByMemberId(Long memberId);

    OmsCartItem getByItemIdAndMemberId(Long itemId, Long memberId);

    Long save(OmsCartRequestParam requestParam);

    boolean update(OmsCartRequestParam requestParam);

    boolean deleteByIds(Set<Long> ids);

    boolean deleteByItemIds(Set<Long> ids);

    boolean delCartItemByItemIds(List<String> itemIds);
}
