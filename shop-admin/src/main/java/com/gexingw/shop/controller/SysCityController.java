package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.dto.city.SysCityRequestParam;
import com.gexingw.shop.dto.city.SysCitySearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.SysCityService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("sys/city")
public class SysCityController {
    @Autowired
    SysCityService cityService;

    @GetMapping
    R index(SysCitySearchParam searchParam) {
        // 分页
        IPage<SysCity> page = new Page<>(searchParam.getPage(), searchParam.getSize());
        // 查询条件
        QueryWrapper<SysCity> queryWrapper = new QueryWrapper<>();
        if (searchParam.getBlurry() != null) {   // 名字搜索
            queryWrapper.like("name", searchParam.getBlurry());
        }

        if (searchParam.getParentCode() != null) {   // 父Id
            queryWrapper.eq("parent_code", searchParam.getParentCode());
        }

        return R.ok(PageUtil.format(cityService.searchList(queryWrapper, page)));
    }

    @PostMapping
    R save(@RequestBody SysCityRequestParam requestParam) {
        Integer cityId = cityService.save(requestParam);
        return cityId != null ? R.ok("已保存！") : R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
    }

    @PutMapping
    R update(@RequestBody SysCityRequestParam requestParam) {
        return cityService.update(requestParam) ? R.ok("已更新！"):R.ok(RespCode.UPDATE_FAILURE.getCode(), "保存失败！");
    }

    @DeleteMapping
    R delete(Set<Integer> ids) {
        return cityService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
