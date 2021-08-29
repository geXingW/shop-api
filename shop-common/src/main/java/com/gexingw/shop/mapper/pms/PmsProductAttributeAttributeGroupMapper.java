package com.gexingw.shop.mapper.pms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.bo.pms.PmsProductAttributeAttributeGroup;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PmsProductAttributeAttributeGroupMapper extends BaseMapper<PmsProductAttributeAttributeGroup> {

    @Insert("<script>" +
            "INSERT INTO `pms_product_attribute_attribute_group` ( `product_attribute_group_id`, `product_attribute_id` ) values " +
            "<foreach collection='attributeIds' item='attributeId' index='index' separator=','>" +
            "(#{groupId}, #{attributeId})" +
            "</foreach>" +
            "</script>")
    Integer batchSaveGroupAttributes(Long groupId, List<Long> attributeIds);
}
