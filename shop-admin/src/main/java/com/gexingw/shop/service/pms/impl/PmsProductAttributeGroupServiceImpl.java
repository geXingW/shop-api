package com.gexingw.shop.service.pms.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttributeAttributeGroup;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.dto.product.PmsProductAttributeGroupRequestParam;
import com.gexingw.shop.mapper.pms.PmsProductAttributeAttributeGroupMapper;
import com.gexingw.shop.mapper.pms.PmsProductAttributeGroupMapper;
import com.gexingw.shop.service.pms.PmsProductAttributeGroupService;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PmsProductAttributeGroupServiceImpl implements PmsProductAttributeGroupService {

    @Resource
    PmsProductAttributeGroupMapper groupMapper;

    @Resource
    PmsProductAttributeAttributeGroupMapper attributeAttributeGroupMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public IPage<PmsProductAttributeGroup> search(QueryWrapper<PmsProductAttributeGroup> queryWrapper, Page<PmsProductAttributeGroup> page) {
        return groupMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long save(PmsProductAttributeGroupRequestParam requestParam) {
        PmsProductAttributeGroup attributeGroup = new PmsProductAttributeGroup();
        BeanUtils.copyProperties(requestParam, attributeGroup);

        if (groupMapper.insert(attributeGroup) <= 0) {
            return null;
        }

        return attributeGroup.getId();
    }

    @Override
    public Integer getCntById(Long id) {
        return groupMapper.selectCount(new QueryWrapper<PmsProductAttributeGroup>().eq("id", id));
    }

    @Override
    public boolean update(PmsProductAttributeGroupRequestParam requestParam) {
        PmsProductAttributeGroup attributeGroup = new PmsProductAttributeGroup();
        BeanUtils.copyProperties(requestParam, attributeGroup);

        return groupMapper.updateById(attributeGroup) > 0;
    }

    @Override
    public boolean deleteByBatchIds(Set<Long> ids) {
        return groupMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Long> getAttachAttributeIdsByGroupId(Long groupId) {
        // 查询与该group绑定的属性Id
        QueryWrapper<PmsProductAttributeAttributeGroup> queryWrapper = new QueryWrapper<PmsProductAttributeAttributeGroup>()
                .eq("product_attribute_group_id", groupId).select("product_attribute_id");
        return attributeAttributeGroupMapper.selectList(queryWrapper).stream()
                .map(PmsProductAttributeAttributeGroup::getProductAttributeId).collect(Collectors.toList());
    }

    @Override
    public boolean attachAttributeToGroup(Long groupId, List<Long> attributeIds) {
        return attributeAttributeGroupMapper.batchSaveGroupAttributes(groupId, attributeIds) > 0;
    }

    @Override
    public boolean detachAttributeToGroup(Long groupId, Set<Long> attributeIds) {
        QueryWrapper<PmsProductAttributeAttributeGroup> queryWrapper = new QueryWrapper<PmsProductAttributeAttributeGroup>()
                .eq("product_attribute_group_id", groupId).in("product_attribute_id", attributeIds);
        return attributeAttributeGroupMapper.delete(queryWrapper) > 0;
    }

    @Override
    public PmsProductAttributeGroup findById(Long id) {
        // 尝试从redis获取
        PmsProductAttributeGroup attributeGroup = getGroupFromRedisById(id);
        if (attributeGroup != null) {
            return attributeGroup;
        }

        attributeGroup = groupMapper.selectById(id);
        if (attributeGroup != null) {
            setGroupToRedisById(id, attributeGroup);
        }

        return attributeGroup;
    }

    @Override
    public List<PmsProductAttributeGroup> getAttributeGroupsByCategoryId(Long categoryId) {
        QueryWrapper<PmsProductAttributeGroup> queryWrapper = new QueryWrapper<PmsProductAttributeGroup>()
                .eq("category_id", categoryId);
        return groupMapper.selectList(queryWrapper);
    }

    @Override
    public List<Long> getAttachAttributeIdsByGroupIds(List<Long> attributeGroupIds) {
        // 查询与该group绑定的属性Id
        QueryWrapper<PmsProductAttributeAttributeGroup> queryWrapper = new QueryWrapper<PmsProductAttributeAttributeGroup>()
                .in("product_attribute_group_id", attributeGroupIds).select("product_attribute_id");
        return attributeAttributeGroupMapper.selectList(queryWrapper).stream()
                .map(PmsProductAttributeAttributeGroup::getProductAttributeId).collect(Collectors.toList());
    }

    public PmsProductAttributeGroup getGroupFromRedisById(Long id) {
        Object redisObj = redisUtil.get(String.format(ProductConstant.REDIS_PRODUCT_ATTRIBUTE_GROUP_FORMAT, id));
        return redisObj != null ? JSON.parseObject(redisObj.toString(), PmsProductAttributeGroup.class) : null;
    }

    public boolean setGroupToRedisById(Long id, PmsProductAttributeGroup attributeGroup) {
        return redisUtil.set(String.format(ProductConstant.REDIS_PRODUCT_ATTRIBUTE_GROUP_FORMAT, id), attributeGroup);
    }

    public void delGroupFromRedisById(Long id) {
        redisUtil.del(String.format(ProductConstant.REDIS_PRODUCT_ATTRIBUTE_GROUP_FORMAT, id));
    }

}
