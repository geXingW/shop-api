package com.gexingw.shop.service;

import java.util.List;
import java.util.Map;

public interface CityService {
    List<Map<String, Object>> getCityTree();

    List<Map<String, Object>> buildCityTree(Integer parentCode);

    Integer findByName(String region);
}
