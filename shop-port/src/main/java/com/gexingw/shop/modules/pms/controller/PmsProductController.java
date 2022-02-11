package com.gexingw.shop.modules.pms.controller;

import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.enums.PmsProductOnSaleEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class PmsProductController {

    @Autowired
    PmsProductService pmsProductService;

    /**
     * 商品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    R info(@PathVariable Long id) {
        PmsProduct pmsProduct = pmsProductService.getById(id);
        if (pmsProduct == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "商品不存在！");
        }

        // 检查商品是否上架
        Integer isOnSale = Integer.valueOf(pmsProduct.getOnSale());
        if (!PmsProductOnSaleEnum.ON_SALE.getCode().equals(isOnSale)) {
            return R.ok(RespCode.RESOURCE_UNAVAILABLE.getCode(), "商品暂未上架！");
        }

        return R.ok(pmsProduct);
    }
}
