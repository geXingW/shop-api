package com.gexingw.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.constant.SystemConstant;
import com.gexingw.shop.mapper.sys.SysCityMapper;
import com.gexingw.shop.service.CityService;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    SysCityMapper cityMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<Map<String, Object>> getCityTree() {
        List<Map<String, Object>> cityTree = getRedisCityTree();
        if (cityTree != null) { // 尝试从redis中获取
            return cityTree;
        }

        // 从数据库查询
        cityTree = buildCityTree(0);
        if (cityTree != null) { // 写入redis缓存
            setRedisCityTree(cityTree);
        }

        return cityTree;
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
            if (StringUtils.endsWithIgnoreCase(city.getCode().toString(), "00")) {
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

    public boolean setRedisCityTree(List<Map<String, Object>> cityTree) {
        String jsonString = JSON.toJSONString(cityTree);
        return redisUtil.set(SystemConstant.REDIS_SYSTEM_CITY_TREE, jsonString);
    }

    public List<Map<String, Object>> getRedisCityTree() {
        Object redisObj = redisUtil.get(SystemConstant.REDIS_SYSTEM_CITY_TREE);
        if (redisObj == null) {
            return null;
        }

        TypeReference<List<Map<String, Object>>> reference = new TypeReference<List<Map<String, Object>>>() {
        };

        return JSON.parseObject(redisObj.toString(), reference);
    }

    public void delRedisCityTree(){
        redisUtil.del(SystemConstant.REDIS_SYSTEM_CITY_TREE);
    }

    @Override
    public Integer findByName(String region) {
        SysCity city = cityMapper.selectOne(new QueryWrapper<SysCity>().eq("name", region));
        return city != null ? city.getCode() : 0;
    }
}
