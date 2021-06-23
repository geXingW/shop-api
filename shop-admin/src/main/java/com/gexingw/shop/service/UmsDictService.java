package com.gexingw.shop.service;

import com.gexingw.shop.bean.ums.UmsDict;
import com.gexingw.shop.dto.dict.UmsDictRequestParam;

import java.util.List;

public interface UmsDictService {
    UmsDict findByName(String name);

    Long save(UmsDictRequestParam requestParam);

    boolean update(UmsDictRequestParam requestParam);

    boolean delete(List<Long> ids);
}
