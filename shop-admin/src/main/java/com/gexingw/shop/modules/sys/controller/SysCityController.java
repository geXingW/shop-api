package com.gexingw.shop.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.sys.dto.city.SysCityRequestParam;
import com.gexingw.shop.modules.sys.dto.city.SysCitySearchParam;
import com.gexingw.shop.modules.sys.service.SysCityService;
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

        // // 名字模糊搜索
        if (searchParam.getBlurry() != null) {
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
        return R.ok(cityService.getCityTree());
    }

    @PostMapping
    R save(@RequestBody SysCityRequestParam requestParam) {
        Integer cityId = cityService.save(requestParam);
        if (cityService.save(requestParam) == null) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        // 删除redis缓存
        cityService.delRedisCityTree();

        return R.ok(RespCode.SYSTEM_CITY_CREATED);
    }

    @PutMapping
    R update(@RequestBody SysCityRequestParam requestParam) {
        if (!cityService.update(requestParam)) {
            R.failure(RespCode.UPDATE_FAILURE, "更新失败！");
        }

        // 删除redis缓存
        cityService.delRedisCityTree();

        return R.ok(RespCode.SYSTEM_CITY_UPDATED);
    }

    @DeleteMapping
    R delete(@RequestBody Set<Integer> ids) {
        if (!cityService.deleteByIds(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        // 删除redis缓存
        cityService.delRedisCityTree();

        return R.ok(RespCode.SYSTEM_CITY_DELETED);
    }
}
