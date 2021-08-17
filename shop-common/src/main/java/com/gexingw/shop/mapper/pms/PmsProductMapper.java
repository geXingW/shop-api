package com.gexingw.shop.mapper.pms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.pms.PmsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PmsProductMapper extends BaseMapper<PmsProduct> {
    @Update("update pms_products set `stock` = `stock` - #{stock} where id = #{id}")
    Integer decrStock(Long id, Integer stock);
}
