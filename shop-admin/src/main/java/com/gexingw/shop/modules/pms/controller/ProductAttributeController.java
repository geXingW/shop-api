package com.gexingw.shop.modules.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeRequestParam;
import com.gexingw.shop.modules.pms.dto.attribute.PmsProductAttributeSearchParam;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeService;
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

        return id != null ? R.ok(id) : R.failure(RespCode.SAVE_FAILURE);
    }

    @PutMapping("/{id}")
    public R update(@RequestBody PmsProductAttributeRequestParam requestParam) {
        PmsProductAttribute attribute = attributeService.findById(requestParam.getId());
        if (attribute == null) {
            return R.ok(RespCode.PRODUCT_ATTRIBUTE_NOT_EXIST);
        }

        return attributeService.update(requestParam) ? R.ok(RespCode.PRODUCT_ATTRIBUTE_UPDATED) : R.failure(RespCode.UPDATE_FAILURE);
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        return attributeService.deleteByIds(ids) ? R.ok(RespCode.PRODUCT_ATTRIBUTE_DELETED) : R.failure(RespCode.DELETE_FAILURE);
    }
}
