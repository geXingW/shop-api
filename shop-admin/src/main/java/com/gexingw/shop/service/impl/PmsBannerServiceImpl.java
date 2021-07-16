package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.dto.banner.BannerRequestParam;
import com.gexingw.shop.mapper.pms.PmsBannerMapper;
import com.gexingw.shop.service.PmsBannerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmsBannerServiceImpl implements PmsBannerService {

    @Autowired
    PmsBannerMapper bannerMapper;

    @Override
    public IPage<PmsBanner> search(QueryWrapper<PmsBanner> queryWrapper, IPage<PmsBanner> page) {
        return bannerMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long save(BannerRequestParam requestParam) {
        PmsBanner pmsBanner = new PmsBanner();
        BeanUtils.copyProperties(requestParam, pmsBanner);

        if(bannerMapper.insert(pmsBanner) <= 0){
            return null;
        }

        return pmsBanner.getId();
    }
}
