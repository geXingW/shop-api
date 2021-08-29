package com.gexingw.shop.controller.pms;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.dto.product.PmsProductAttributeGroupRequestParam;
import com.gexingw.shop.dto.product.PmsProductAttributeGroupSearchParam;
import com.gexingw.shop.dto.product.PmsProductAttributeRequestParam;
import com.gexingw.shop.dto.product.PmsProductAttributeSearchParam;
import com.gexingw.shop.enums.PmsProductAttributeTypeEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.PmsProductAttributeService;
import com.gexingw.shop.service.PmsProductCategoryService;
import com.gexingw.shop.service.pms.PmsProductAttributeGroupService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.vo.pms.PmsProductAttributeGroupVO;
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
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
        }

        return R.ok();
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody PmsProductAttributeGroupRequestParam requestParam) {
        if (groupService.getCntById(id) <= 0) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "属性分组不存在！");
        }

        if (!groupService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        if (!groupService.deleteByBatchIds(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        return R.ok("已删除！");
    }

    @GetMapping("/attach-attribute")
    public R getAttachAttribute(PmsProductAttributeGroupSearchParam searchParam) {
//        Page<PmsProductAttribute> page = new Page<>(searchParam.getPage(), searchParam.getSize());
        Page<PmsProductAttribute> page = new Page<>(searchParam.getPage(), 100); // 默认最多返回100个

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
            if (attachAttributeIds.size() == 0) { // 没有已绑定ID,直接返回空结果
                return R.ok(PageUtil.format(page));
            }

            queryWrapper.in("id", attachAttributeIds);
        }

        if (!searchParam.isAttached() && attachAttributeIds.size() > 0) {
            queryWrapper.notIn("id", attachAttributeIds);
        }

        // 属性模糊搜索
        if(searchParam.getBlurry() != null){
            queryWrapper.like("name", searchParam.getBlurry());
        }

        // 查询同一分类下的属性
        queryWrapper.eq("category_id", attributeGroup.getCategoryId());

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
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
        }

        return R.ok("已保存！");
    }

    @DeleteMapping("/attach-attribute/{groupId}")
    public R delAttachAttribute(@PathVariable Long groupId, @RequestBody Set<Long> attributeIds) {
        if (!groupService.detachAttributeToGroup(groupId, attributeIds)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        return R.ok("已删除！");
    }
}
