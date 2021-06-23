package com.gexingw.shop.service;

import com.gexingw.shop.bean.ums.UmsDictDetail;
import com.gexingw.shop.dto.dict.UmsDictDetailRequestParam;

import java.util.List;

public interface UmsDictDetailService {
    List<UmsDictDetail> getByDictId(long dictId);

    Long save(UmsDictDetailRequestParam dictDetailRequestParam);

    boolean update(UmsDictDetailRequestParam dictDetailRequestParam);
}
