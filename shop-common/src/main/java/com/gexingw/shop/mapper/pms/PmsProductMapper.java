package com.gexingw.shop.mapper.pms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.pms.PmsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PmsProductMapper extends BaseMapper<PmsProduct> {
    @Update("update pms_products set `stock` = `stock` - #{stock} where id = #{id}")
    Integer decrStock(String id, Integer stock);

    @Update("UPDATE `pms_products` SET `stock` = `stock` - ${quantity} WHERE `id` = ${id}")
    int lockStock(String id, Integer quantity);

    @Update("UPDATE pms_products SET sale_cnt = sale_cnt + ${quantity} WHERE id = ${id}")
    boolean addSaleCount(String id, Integer quantity);
}
