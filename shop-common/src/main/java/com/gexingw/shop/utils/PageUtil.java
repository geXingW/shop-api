package com.gexingw.shop.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单分页模型
 *
 * @author hubin
 * @since 2018-06-09
 */
public class PageUtil {
    public static Map<String, Object> format(IPage<?> _result) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("records", _result.getRecords());
        result.put("total", _result.getTotal());
        result.put("size", _result.getSize());
        result.put("page", _result.getCurrent());

        return result;
    }
}
