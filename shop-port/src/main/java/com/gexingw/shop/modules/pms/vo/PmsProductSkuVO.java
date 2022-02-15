package com.gexingw.shop.modules.pms.vo;

import com.gexingw.shop.bo.pms.PmsProductSku;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PmsProductSkuVO {
    private String productId;

    private BigDecimal price = new BigDecimal(0);

    private int stock = 0;

    private int lowStock = 0;

    public PmsProductSkuVO() {

    }

    public PmsProductSkuVO(PmsProductSku productSku) {
        this.setProductSkuInfo(productSku);
    }

    public void setProductSkuInfo(PmsProductSku productSku) {
        this.productId = productSku.getProductId();
        this.price = productSku.getPrice();
        this.stock = productSku.getStock();
        this.lowStock = productSku.getLowStock();
    }
}
