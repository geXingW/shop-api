package com.gexingw.shop.modules.oms.vo;

import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OmsOrderDetailVO {
    private String id;

    private Long memberId;

    private Integer sourceType;

    private Integer status = 0;

    private String note;

    private BigDecimal totalAmount;

    private BigDecimal itemAmount;

    private BigDecimal payAmount;

    private BigDecimal freightAmount;

    private List<Item> items = new ArrayList<>();

    private RecvAddress recvAddress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Item {
        private String id;

        private String name;

        private Integer quantity;

        private BigDecimal price;

        private BigDecimal amount;

        private String pic;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RecvAddress {
        private String name;

        private String phone;

        private String detailAddress;
    }

    public void setOrderItems(List<OmsOrderItemDetail> orderItemDetails, FileConfig fileConfig) {
        for (OmsOrderItemDetail orderItemDetail : orderItemDetails) {
            Item orderItem = new Item();
            orderItem.setId(orderItemDetail.getItemId());
            orderItem.setName(orderItemDetail.getItemName());
            orderItem.setQuantity(orderItemDetail.getItemQuantity());
            orderItem.setPrice(orderItemDetail.getItemPrice());
            orderItem.setAmount(orderItemDetail.getItemPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItem.setPic(this.getItemPic(orderItemDetail.getItemPic(), fileConfig));

            this.items.add(orderItem);
        }
    }

    public void setRecvAddress(OmsOrderRecvAddress orderRecvAddress) {
        String fullAddress = orderRecvAddress.getProvinceName() + orderRecvAddress.getCityName() + orderRecvAddress.getRegionName()
                + orderRecvAddress.getDetailAddress();
        recvAddress = new RecvAddress(orderRecvAddress.getName(), orderRecvAddress.getPhoneNumber(), fullAddress);
    }

    private String getItemPic(String picUri, FileConfig fileConfig) {
        String domain = fileConfig.getDiskHost();

        String separator = File.separator;
        return StringUtil.trim(domain, separator) + separator + picUri;
    }
}
