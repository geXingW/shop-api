package com.gexingw.shop.service;

import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.dto.oms.OmsCartRequestParam;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.vo.Oms.OmsCartItemVo;

import java.util.List;
import java.util.Set;

public interface CartService {
    List<OmsCartItemVo> getListByMemberId(Long memberId);

    OmsCartItem getByItemIdAndMemberId(Long itemId, Long memberId);

    Long save(OmsCartRequestParam requestParam);

    boolean update(OmsCartRequestParam requestParam);

    boolean deleteByIds(Set<Long> ids);
}
