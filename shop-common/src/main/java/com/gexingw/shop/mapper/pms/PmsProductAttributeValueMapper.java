package com.gexingw.shop.mapper.pms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.pms.PmsProductAttributeValue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface PmsProductAttributeValueMapper extends BaseMapper<PmsProductAttributeValue> {

    @Insert("<script>" +
            "INSERT INTO pms_product_attribute_value(`product_id`, `product_attribute_id`, `product_attribute_value`) VALUES " +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "(#{item.productId}, #{item.attributeId}, #{item.attributeValue})" +
            "</foreach>" +
            "</script>")
    int batchSave(ArrayList<Map<String, Object>> list);

    @Delete("DELETE FROM pms_product_attribute_value WHERE `product_id` = #{productId}")
    int deleteByProductId(Long productId);

    @Update("UPDATE pms_product_attribute_value SET `product_attribute_name` = #{productAttributeName} WHERE `product_attribute_id` = #{productAttributeId}")
    int updateAttributeNameByAttributeId(Long productAttributeId, String productAttributeName);
}
