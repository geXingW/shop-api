package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.dto.banner.BannerRequestParam;

public interface PmsBannerService {
    IPage<PmsBanner> search(QueryWrapper<PmsBanner> queryWrapper, IPage<PmsBanner> page);

    Long save(BannerRequestParam requestParam);
}
