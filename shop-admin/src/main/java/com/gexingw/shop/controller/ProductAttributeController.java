package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.dto.product.PmsProductAttributeRequestParam;
import com.gexingw.shop.dto.product.PmsProductAttributeSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.PmsProductAttributeService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/product/attribute")
public class ProductAttributeController {

    @Autowired
    PmsProductAttributeService attributeService;

    @GetMapping
    public R list(PmsProductAttributeSearchParam searchParam) {
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();

        Page<PmsProductAttribute> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        return R.ok(PageUtil.format(attributeService.searchList(queryWrapper, page)));
    }

    @PostMapping
    public R save(@RequestBody PmsProductAttributeRequestParam requestParam) {
        Long id = attributeService.save(requestParam);

        return id != null ? R.ok(id) : R.ok(RespCode.SAVE_FAILURE.getCode(), "添加失败！");
    }

    @PutMapping("/{id}")
    public R update(@RequestBody PmsProductAttributeRequestParam requestParam) {
        PmsProductAttribute attribute = attributeService.findById(requestParam.getId());
        if (attribute == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "商品属性未找到！");
        }

        return attributeService.update(requestParam) ? R.ok("已更新！") : R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        return attributeService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
