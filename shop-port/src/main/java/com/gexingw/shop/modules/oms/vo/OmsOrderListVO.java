package com.gexingw.shop.modules.oms.vo;

import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.utils.StringUtil;
import lombok.Data;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OmsOrderListVO {

    private String id;

    // 支付价格
    private BigDecimal payAmount;

    // 订单状态
    private Integer status;

    // 付款方式
    private Integer payType;

    // 订单商品信息
    private List<Item> items = new ArrayList<>();

    // 收获地址
    private RecvAddress recvAddress;

    @Data
    public static class Item {

        private String id;

        private String title;

        private String pic;

        private Integer itemQuantity;
    }

    @Data
    public static class RecvAddress {

        private String id;

        private String name;
    }

    public OmsOrderListVO setOrderItems(List<OmsOrderItemDetail> orderItems, FileConfig fileConfig) {
        for (OmsOrderItemDetail orderItem : orderItems) {
            Item item = new Item();
            item.setId(orderItem.getItemId());
            item.setTitle(orderItem.getItemName());
            item.setItemQuantity(orderItem.getItemQuantity());
            item.setPic(this.getItemPic(orderItem.getItemPic(), fileConfig));

            this.items.add(item);
        }

        return this;
    }

    private String getItemPic(String picUri, FileConfig fileConfig) {
        String domain = fileConfig.getDiskHost();

        String separator = File.separator;
        return StringUtil.trim(domain, separator) + separator + picUri;
    }

    public OmsOrderListVO setOrderRecvAddress(OmsOrderRecvAddress recvAddress) {
        RecvAddress address = new RecvAddress();
        address.setId(recvAddress.getId());
        address.setName(recvAddress.getName());
        this.recvAddress = address;

        return this;
    }

}
