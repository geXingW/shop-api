package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.oms.OmsCartItem;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchRequestParam;

import java.util.ArrayList;
import java.util.List;

public interface PmsProductService {
    boolean isExist(Long id);

    PmsProduct getById(String id);

    List<PmsProduct> getByIds(List<String> ids);

    IPage<PmsProduct> search(PmsProductSearchRequestParam requestParam);

    boolean lockStockByProductId(String id, Integer quantity);

    boolean addProductSaleCount(ArrayList<OmsCartItem> cartItems);
}
