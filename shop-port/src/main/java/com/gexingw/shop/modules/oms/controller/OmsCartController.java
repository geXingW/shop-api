package com.gexingw.shop.modules.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.oms.dto.OmsCartRequestParam;
import com.gexingw.shop.modules.oms.dto.OmsCartSearchParam;
import com.gexingw.shop.modules.oms.service.CartService;
import com.gexingw.shop.modules.oms.vo.OmsCartVO;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("cart")
public class OmsCartController {

    @Autowired
    CartService cartService;

    @Autowired
    PmsProductService productService;

    @Autowired
    PmsProductSkuService productSkuService;

    @Autowired
    FileConfig fileConfig;

    @GetMapping
    R index(OmsCartSearchParam searchParam) {
        // 查询购物车商品信息
        Long memberId = AuthUtil.getAuthId();

        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<>();
        // 按照更新时间 + 添加购物车的时间排序
        queryWrapper.orderByDesc("update_time", "create_time");

        if (searchParam.getChecked() != null) {
            queryWrapper.eq("checked", searchParam.getChecked());
        }

        List<OmsCartItem> cartItems = cartService.search(queryWrapper);

        // 购物车总价
        BigDecimal cartTotalPrice = new BigDecimal(0);
        Long cartItemCount = 0L;

        List<OmsCartVO> cartVOs = new ArrayList<>();
        for (OmsCartItem cartItem : cartItems) {

            int itemStock = 0;
            if (cartItem.getSkuId() != 0) {
                itemStock = productSkuService.getById(cartItem.getSkuId()).getStock();
            } else {
                itemStock = productService.getById(cartItem.getItemId()).getStock();
            }

            OmsCartVO cartVO = new OmsCartVO(cartItem, itemStock, fileConfig);

            // 如果商品被选中，累加商品价格
            if (cartItem.getChecked() == 1) {
                cartTotalPrice = cartTotalPrice.add(cartVO.getItemTotalPrice());
                cartItemCount += cartItem.getItemQuantity();
            }

            cartVOs.add(cartVO);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("cartTotalPrice", cartTotalPrice);
        result.put("cartItems", cartVOs);
        result.put("cartItemCount", cartItemCount);

        return R.ok(result);
    }

    @PostMapping
    R save(@RequestBody OmsCartRequestParam requestParam) {
        // 检查商品信息
        // todo 此处应该检查商品的上架和删除状态
        PmsProduct product = productService.getById(requestParam.getProductId());
        if (product == null) {
            return R.failure(RespCode.RESOURCE_NOT_EXIST);
        }

        Integer stock = 0;
        PmsProductSku productSku = null;

        // 无规格商品
        if (requestParam.getSkuId() == 0) {
            stock = product.getStock();
        } else {
            // 检查商品规格
            productSku = productSkuService.getById(requestParam.getSkuId());
            if (productSku == null) {
                return R.failure(RespCode.RESOURCE_NOT_EXIST);
            }
            stock = productSku.getStock();
        }

        // 检查商品库存
        if (requestParam.getProductCnt() > stock) {
            return R.failure(RespCode.PRODUCT_STOCK_OVER);
        }

        // 查看当前商品在购物车中是否存在
        OmsCartItem cartItem = cartService.getBySkuIdAndMemberId(requestParam.getSkuId(), requestParam.getMemberId());

        // 购物车中不存在该规格的商品，新增购物车
        if (cartItem == null) {
            boolean saved = productSku == null ? cartService.save(requestParam, product, null) : cartService.save(requestParam, product, productSku);
            return saved ? R.ok(RespCode.CART_ADDED) : R.failure(RespCode.SAVE_FAILURE);
        }

        // 检查商品库存
        if (requestParam.getProductCnt() > stock - cartItem.getItemQuantity()) {
            return R.failure(RespCode.PRODUCT_STOCK_OVER);
        }

        // 已存在该规格商品的，增加购物车商品数量
        cartItem.setItemQuantity(requestParam.getProductCnt() + cartItem.getItemQuantity());
        if (!cartService.update(cartItem)) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        return R.ok(RespCode.CART_ADDED);
    }

    @PutMapping
    R update(@RequestBody OmsCartRequestParam requestParam) {
        // 检查购物车信息是否存在
        OmsCartItem cartItem = cartService.getById(requestParam.getId());
        if (cartItem == null) {
            return R.failure(RespCode.CART_ITEM_NOT_EXIST);
        }

        PmsProduct product = productService.getById(cartItem.getItemId());
        if (product == null) {
            return R.ok(RespCode.PRODUCT_NOT_EXIST);
        }

        Integer stock = 0;
        PmsProductSku productSku = null;

        // 无规格商品
        if (requestParam.getSkuId() == 0) {
            stock = product.getStock();
        } else {
            // 检查商品规格
            productSku = productSkuService.getById(requestParam.getSkuId());
            if (productSku == null) {
                return R.failure(RespCode.PRODUCT_SKU_NOT_EXIST);
            }
            stock = productSku.getStock();
        }

        // 检查商品库存
        if (requestParam.getProductCnt() > stock) {
            return R.failure(RespCode.PRODUCT_STOCK_OVER);
        }

        if (!cartService.update(cartItem, productSku, requestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        return R.ok(RespCode.CART_UPDATED);
    }

    @PutMapping("change-check-status")
    R changeCartItemCheckStatus(@RequestBody Set<Long> ids) {
        if (!cartService.changeCheckStatusByIds(ids)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        return R.ok(RespCode.CART_UPDATED);
    }

    @DeleteMapping
    R delete(@RequestBody Set<Long> ids) {
        if (ids.isEmpty()) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        return cartService.deleteByIds(ids) ? R.ok(RespCode.CART_DELETED) : R.failure(RespCode.DELETE_FAILURE);
    }

    @DeleteMapping("clear")
    public R clear() {
        if (!cartService.clearCartItems()) {
            return R.failure(RespCode.DELETE_FAILURE);
        }
        return R.ok(RespCode.CART_DELETED);
    }
}
