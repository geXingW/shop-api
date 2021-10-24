package com.gexingw.shop.modules.sys.service;

import com.gexingw.shop.bo.ums.UmsDict;
import com.gexingw.shop.modules.sys.dto.dict.UmsDictRequestParam;

import java.util.List;

public interface UmsDictService {
    UmsDict findByName(String name);

    Long save(UmsDictRequestParam requestParam);

    boolean update(UmsDictRequestParam requestParam);

    boolean delete(List<Long> ids);
}
