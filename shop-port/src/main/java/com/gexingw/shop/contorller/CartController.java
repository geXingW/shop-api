package com.gexingw.shop.contorller;

import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.dto.oms.OmsCartRequestParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.CartService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping
    R index() {
        Long memberId = AuthUtil.getAuthId();

        return R.ok(cartService.getListByMemberId(memberId));
    }

    @PostMapping
    R save(@RequestBody OmsCartRequestParam requestParam) {
        OmsCartItem cartItem = cartService.getByItemIdAndMemberId(requestParam.getItemId(), requestParam.getMemberId());
        if (cartItem == null) { // 已存在的更新数量
            return cartService.save(requestParam) != null ? R.ok("已添加！") : R.ok(RespCode.SAVE_FAILURE.getCode(), "添加失败！");
        }

        if (!cartService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "添加失败！");
        }

        return R.ok("已添加！");
    }

    @PutMapping
    R update(@RequestBody OmsCartRequestParam requestParam) {
        OmsCartItem cartItem = cartService.getByItemIdAndMemberId(requestParam.getItemId(), requestParam.getMemberId());
        if (cartItem == null) { // 购物车不存在当前商品的，直接保存
            return cartService.save(requestParam) != null ? R.ok("已更新！") : R.ok(RespCode.SAVE_FAILURE.getCode(), "更新失败！");
        }

        if (!cartService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    R delete(@RequestBody Set<Long> ids) {
        return cartService.deleteByIds(ids) ? R.ok("已移除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "移除失败！");
    }
}
