package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.dto.city.SysCityRequestParam;
import com.gexingw.shop.mapper.sys.SysCityMapper;
import com.gexingw.shop.service.SysCityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysCityServiceImpl implements SysCityService {
    @Autowired
    SysCityMapper cityMapper;

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        return cityMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public IPage<SysCity> searchList(QueryWrapper<SysCity> queryWrapper, IPage<SysCity> page) {
        return cityMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Integer save(SysCityRequestParam requestParam) {
        SysCity sysCity = new SysCity();
        BeanUtils.copyProperties(requestParam, sysCity);
        if (cityMapper.insert(sysCity) <= 0) {
            return null;
        }

        return sysCity.getId();
    }

    @Override
    public boolean update(SysCityRequestParam requestParam) {
        SysCity sysCity = new SysCity();
        BeanUtils.copyProperties(requestParam, sysCity);

        return cityMapper.updateById(sysCity) > 0;
    }

    @Override
    public List<SysCity> getListByParentCode(Integer code) {
        return cityMapper.selectList(new QueryWrapper<SysCity>().eq("parent_code", code));
    }

    @Override
    public SysCity findById(Long id) {
        return cityMapper.selectById(id);
    }

    /**
     * 从顶级递归往上下查
     * 当查询结果的 `parent_code` 时，递归结束
     *
     * @param parentCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getPeerAndParentListByParentCode(Integer parentCode, Integer targetParentCode) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();

        // 如果直接查询顶级节点，直接返回parent_code 等于0的所有城市列表
        List<SysCity> cities = cityMapper.selectList(new QueryWrapper<SysCity>().eq("parent_code", parentCode));

        // 当前查询到的所有城市的父级code
        List<Integer> parentCodes = cities.stream().map(SysCity::getParentCode).distinct().collect(Collectors.toList());

        for (SysCity city : cities) {
            HashMap<String, Object> item = new HashMap<>();

            // 查找下级城市
            // 满足以下两个条件，即判定为已找到该级和所有上级城市,否则继续查找下一级
            // 1. 如果当前查找的城市父级code为0；
            // 2.或者本次查找到的parentCode中包含要查找的parentCode
            if (!parentCodes.contains(targetParentCode) && 0 != targetParentCode) {
                item.put("children", getPeerAndParentListByParentCode(city.getCode(), targetParentCode));
            }

            item.put("id", city.getId());
            item.put("name", city.getName());
            item.put("label", city.getName());
            item.put("parent_code", city.getParentCode());

            result.add(item);
        }

        return result;
    }
}
