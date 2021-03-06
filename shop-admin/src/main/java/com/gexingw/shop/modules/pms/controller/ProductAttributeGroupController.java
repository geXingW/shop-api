package com.gexingw.shop.modules.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.enums.PmsProductAttributeTypeEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeGroupRequestParam;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeGroupSearchParam;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeSearchParam;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeGroupService;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeService;
import com.gexingw.shop.modules.pms.service.PmsProductCategoryService;
import com.gexingw.shop.modules.pms.vo.attribute.PmsProductAttributeGroupVO;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("product/attribute-group")
public class ProductAttributeGroupController {

    @Autowired
    PmsProductAttributeGroupService groupService;

    @Autowired
    PmsProductCategoryService categoryService;

    @Autowired
    PmsProductAttributeService attributeService;

    @GetMapping
    public R index(PmsProductAttributeSearchParam searchParam) {
        // 查询条件
        QueryWrapper<PmsProductAttributeGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(true, searchParam.sortAsc(), searchParam.sortField());

        // 分页
        Page<PmsProductAttributeGroup> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        IPage<PmsProductAttributeGroup> searchResult = groupService.search(queryWrapper, page);
        // 格式化返回结果
        List<PmsProductAttributeGroup> productAttributeGroups = searchResult.getRecords();
        ArrayList<PmsProductAttributeGroupVO> productAttributeGroupVOS = new ArrayList<>();
        for (PmsProductAttributeGroup productAttributeGroup : productAttributeGroups) {
            // 查询分类信息
            PmsProductCategory productCategory = categoryService.getById(productAttributeGroup.getCategoryId());

            PmsProductAttributeGroupVO productAttributeGroupVO = new PmsProductAttributeGroupVO();
            productAttributeGroupVO.setId(productAttributeGroup.getId());
            productAttributeGroupVO.setName(productAttributeGroup.getName());
            productAttributeGroupVO.setCategoryId(productAttributeGroup.getCategoryId());
            productAttributeGroupVO.setCategoryName(productCategory != null ? productCategory.getName() : "");
            productAttributeGroupVO.setSort(productAttributeGroup.getSort());
            productAttributeGroupVO.setCreateTime(productAttributeGroup.getCreateTime());

            productAttributeGroupVOS.add(productAttributeGroupVO);
        }

        Map<String, Object> result = PageUtil.format(searchResult);
        result.put("records", productAttributeGroupVOS);

        return R.ok(result);
    }

    @PostMapping
    public R save(@RequestBody PmsProductAttributeGroupRequestParam requestParam) {
        Long id = groupService.save(requestParam);
        if (id == null) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        return R.ok(RespCode.PRODUCT_ATTRIBUTE_GROUP_CREATED);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody PmsProductAttributeGroupRequestParam requestParam) {
        if (groupService.getCntById(id) <= 0) {
            return R.failure(RespCode.PRODUCT_ATTRIBUTE_GROUP_NOT_EXIST);
        }

        if (!groupService.update(requestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        return R.ok(RespCode.PRODUCT_ATTRIBUTE_GROUP_UPDATED);
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        if (!groupService.deleteByBatchIds(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        return R.ok(RespCode.PRODUCT_ATTRIBUTE_GROUP_DELETED);
    }

    @GetMapping("/attach-attribute")
    public R getAttachAttribute(PmsProductAttributeGroupSearchParam searchParam) {
        // 默认最多返回100个
        Page<PmsProductAttribute> page = new Page<>(searchParam.getPage(), 100);

        // 查询该group信息
        PmsProductAttributeGroup attributeGroup = groupService.findById(searchParam.getGroupId());
        if (attributeGroup == null) {
            return R.ok(PageUtil.format(page));
        }

        // 根据groupId查询所有与该group绑定的属性ID
        List<Long> attachAttributeIds = groupService.getAttachAttributeIdsByGroupId(searchParam.getGroupId());

        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();

        // 查询绑定到该group的属性
        if (searchParam.isAttached()) {
            // 没有已绑定ID,直接返回空结果
            if (attachAttributeIds.size() == 0) {
                return R.ok(PageUtil.format(page));
            }

            queryWrapper.in("id", attachAttributeIds);
        }

        // 查询所有未绑定该group属性
        if (!searchParam.isAttached() && attachAttributeIds.size() > 0) {
            queryWrapper.notIn("id", attachAttributeIds);
        }

        // 属性模糊搜索
        if (searchParam.getBlurry() != null) {
            queryWrapper.like("name", searchParam.getBlurry());
        }

        // 查询基本属性
        if (PmsProductAttributeTypeEnum.BASE_ATTRIBUTE.getCode().equals(searchParam.getType())) {
            queryWrapper.eq("type", PmsProductAttributeTypeEnum.BASE_ATTRIBUTE.getCode());
        }

        // 查询销售属性
        if (PmsProductAttributeTypeEnum.SALE_ATTRIBUTE.getCode().equals(searchParam.getType())) {
            queryWrapper.eq("type", PmsProductAttributeTypeEnum.SALE_ATTRIBUTE.getCode());
        }

        return R.ok(PageUtil.format(attributeService.searchList(queryWrapper, page)));
    }

    @PostMapping("/attach-attribute/{groupId}")
    public R saveAttachAttribute(@PathVariable Long groupId, @RequestBody PmsProductAttributeGroupRequestParam searchParam) {
        if (!groupService.attachAttributeToGroup(groupId, searchParam.getAttributeIds())) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        return R.ok(RespCode.PRODUCT_ATTRIBUTE_UPDATED);
    }

    @DeleteMapping("/attach-attribute/{groupId}")
    public R delAttachAttribute(@PathVariable Long groupId, @RequestBody Set<Long> attributeIds) {
        if (!groupService.detachAttributeToGroup(groupId, attributeIds)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        return R.ok(RespCode.PRODUCT_ATTRIBUTE_GROUP_DELETED);
    }
}
