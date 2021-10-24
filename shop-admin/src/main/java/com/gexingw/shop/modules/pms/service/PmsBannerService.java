package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.modules.sys.dto.banner.BannerRequestParam;

import java.util.List;
import java.util.Set;

public interface PmsBannerService {
    IPage<PmsBanner> search(QueryWrapper<PmsBanner> queryWrapper, IPage<PmsBanner> page);

    Long save(BannerRequestParam requestParam);

    PmsBanner findById(Long id);

    List<PmsBanner> getByIds(Set<Long> ids);

    boolean deleteByIds(Set<Long> ids);

    boolean update(BannerRequestParam requestParam);
}
