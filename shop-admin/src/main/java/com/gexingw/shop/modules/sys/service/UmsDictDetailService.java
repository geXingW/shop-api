package com.gexingw.shop.modules.sys.service;

import com.gexingw.shop.bo.ums.UmsDictDetail;
import com.gexingw.shop.modules.sys.dto.dict.UmsDictDetailRequestParam;

import java.util.List;

public interface UmsDictDetailService {
    List<UmsDictDetail> getByDictId(long dictId);

    Long save(UmsDictDetailRequestParam dictDetailRequestParam);

    boolean update(UmsDictDetailRequestParam dictDetailRequestParam);
}
