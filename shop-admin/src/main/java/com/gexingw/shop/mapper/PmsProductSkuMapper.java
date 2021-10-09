package com.gexingw.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.pms.PmsProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PmsProductSkuMapper extends BaseMapper<PmsProductSku> {
    int batchInsert(List<PmsProductSku> productSkuList);
}
