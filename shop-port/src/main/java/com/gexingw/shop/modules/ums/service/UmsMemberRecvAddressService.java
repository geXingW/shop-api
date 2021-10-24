package com.gexingw.shop.modules.ums.service;

import com.gexingw.shop.bo.ums.UmsMemberRecvAddress;
import com.gexingw.shop.modules.ums.dto.UmsMemberRecvAddressRequestParam;

import java.util.List;
import java.util.Set;

public interface UmsMemberRecvAddressService {
    List<UmsMemberRecvAddress> getListByMemberId(long memberId);

    Long save(UmsMemberRecvAddressRequestParam requestParam);

    boolean deleteByIds(Set<Long> ids);

    boolean update(UmsMemberRecvAddress requestParam);

    boolean updateAddressDefaultStatusExcludeId(Long id);
}
