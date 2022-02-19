package com.gexingw.shop.service;

import com.gexingw.shop.bo.sys.SysCity;

import java.util.List;
import java.util.Map;

public interface CityService {
    List<Map<String, Object>> getCityTree();

    List<Map<String, Object>> buildCityTree(Integer parentCode);

    Integer findByName(String region);

    SysCity findByPCRNames(String province, String city, String region);

    SysCity findByCode(Integer provinceCode);
}
