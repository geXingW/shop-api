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
import org.springframework.util.StringUtils;

import java.util.*;

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
    public List<SysCity> getListByParentCode(Integer parentCode) {
        return cityMapper.selectList(new QueryWrapper<SysCity>().eq("parent_code", parentCode));
    }

    @Override
    public SysCity findById(Long id) {
        return cityMapper.selectById(id);
    }

    @Override
    public List<Map<String, Object>> buildCityTree(Integer parentCode) {
        QueryWrapper<SysCity> queryWrapper = new QueryWrapper<SysCity>().eq("parent_code", parentCode);
        List<SysCity> sysCities = cityMapper.selectList(queryWrapper);

        List<Map<String, Object>> cities = new ArrayList<>();

        for (SysCity city : sysCities) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", city.getId());
            item.put("name", city.getName());
            item.put("label", city.getName());
            item.put("code", city.getCode());
            item.put("parentCode", city.getParentCode());

            // 如果城市代码不是以00结尾的，属于县区一级，不再向下查询
            if(StringUtils.endsWithIgnoreCase(city.getCode().toString(), "00")){
                // 子级城市
                List<Map<String, Object>> children = buildCityTree(city.getCode());
                if (children.size() > 0) {
                    item.put("children", children);
                    item.put("hasChildren", children.size() > 0);
                }
            }

            cities.add(item);
        }

        return cities;
    }
}
