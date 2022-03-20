package com.gexingw.shop.mapper.pms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.pms.PmsProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PmsProductSkuMapper extends BaseMapper<PmsProductSku> {
    int batchInsert(List<PmsProductSku> productSkuList);

    @Update("UPDATE `pms_product_sku` SET `stock` = `stock` - ${quantity} WHERE `id` = ${id}")
    int lockStock(Long id, Integer quantity);
}
