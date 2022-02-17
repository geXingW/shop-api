package com.gexingw.shop.modules.oms.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.constant.OmsCartConstant;
import com.gexingw.shop.utils.StringUtil;
import lombok.Data;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OmsCartVO {
    private Long id;

    private Long memberId;

    private String itemId;

    private BigDecimal itemPrice;

    private Long itemQuantity;

    private String itemPic;

    private String itemTitle;

    private String itemSubTitle;

    private boolean checked;

    private Long skuId;

    private List<SkuItem> skuData = new ArrayList<>();

    private int itemStock;

    private BigDecimal itemTotalPrice;

    public OmsCartVO(OmsCartItem cartItem, PmsProductSku productSku, FileConfig fileConfig) {
        BeanUtil.copyProperties(cartItem, this, "skuData", "itemPic", "checked");

        // 商品库存量
        this.itemStock = productSku.getStock();

        // 解析商品SKU数据
        this.setSkuData(cartItem);

        // 处理商品图片Url
        this.setItemPic(cartItem, fileConfig);

        // 计算商品总价
        this.calcItemTotalPrice(cartItem);

        // 是否选中
        this.checked = OmsCartConstant.CART_ITEM_CHECKED.equals(cartItem.getChecked());
    }

    private void calcItemTotalPrice(OmsCartItem cartItem) {
        this.itemTotalPrice = this.itemPrice.multiply(new BigDecimal(this.itemQuantity));
    }

    private void setItemPic(OmsCartItem cartItem, FileConfig fileConfig) {
        String domain = fileConfig.getDiskHost();

        String separator = File.separator;
        this.itemPic = StringUtil.trim(domain, separator) + separator + cartItem.getItemPic();
    }

    public void setSkuData(OmsCartItem cartItem) {
        TypeReference<List<SkuItem>> typeReference = new TypeReference<List<SkuItem>>() {
        };
        this.skuData = JSON.parseObject(cartItem.getSkuData(), typeReference);
    }

    @Data
    public static class SkuItem {
        private Long id;

        private String name;

        private String value;
    }
}
