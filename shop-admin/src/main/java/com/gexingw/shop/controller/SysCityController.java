package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.dto.city.SysCityRequestParam;
import com.gexingw.shop.dto.city.SysCitySearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.SysCityService;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

        // 模糊搜索
        if (searchParam.getBlurry() != null) {   // 名字搜索
            queryWrapper.like("name", searchParam.getBlurry());
        }

        // 父级城市Id
        Integer parentCode = searchParam.getParentCode() != null ? searchParam.getParentCode() : 0;
        queryWrapper.eq("parent_code", parentCode);

        IPage<SysCity> pageResult = cityService.searchList(queryWrapper, page);
        List<SysCity> pageRecords = pageResult.getRecords();

        ArrayList<Map<String, Object>> records = new ArrayList<>();
        for (SysCity city : pageRecords) {
            HashMap<String, Object> record = new HashMap<>();
            record.put("id", city.getId());
            record.put("name", city.getName());
            record.put("code", city.getCode());
            record.put("parentCode", city.getParentCode());

            // 查询子城市
            List<SysCity> children = cityService.getListByParentCode(city.getCode());
            record.put("hasChildren", children.size() > 0);

            records.add(record);
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("records", records);
        result.put("total", pageResult.getTotal());
        result.put("size", pageResult.getSize());
        result.put("page", pageResult.getCurrent());

        return R.ok(result);
    }

    @GetMapping("build-tree")
    R buildTree(SysCitySearchParam searchParam) {
        // 城市层级列表
        return R.ok(cityService.buildCityTree(0));
    }

    @PostMapping
    R save(@RequestBody SysCityRequestParam requestParam) {
        Integer cityId = cityService.save(requestParam);
        return cityId != null ? R.ok("已保存！") : R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
    }

    @PutMapping
    R update(@RequestBody SysCityRequestParam requestParam) {
//        // 检查code是否已存在
//        if(cityService.findById(requestParam.getCo)){
//
//        }

        return cityService.update(requestParam) ? R.ok("已更新！") : R.ok(RespCode.UPDATE_FAILURE.getCode(), "保存失败！");
    }

    @DeleteMapping
    R delete(@RequestBody Set<Integer> ids) {
        return cityService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
