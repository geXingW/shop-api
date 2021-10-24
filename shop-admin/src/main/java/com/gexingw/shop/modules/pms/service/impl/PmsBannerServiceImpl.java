package com.gexingw.shop.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.modules.sys.dto.banner.BannerRequestParam;
import com.gexingw.shop.mapper.pms.PmsBannerMapper;
import com.gexingw.shop.modules.pms.service.PmsBannerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

        if (bannerMapper.insert(pmsBanner) <= 0) {
            return null;
        }

        return pmsBanner.getId();
    }

    @Override
    public PmsBanner findById(Long id) {
        return bannerMapper.selectById(id);
    }

    @Override
    public List<PmsBanner> getByIds(Set<Long> ids) {
        return bannerMapper.selectBatchIds(ids);
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        return bannerMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean update(BannerRequestParam requestParam) {
        PmsBanner banner = bannerMapper.selectById(requestParam.getId());
        BeanUtils.copyProperties(requestParam, banner);

        return bannerMapper.updateById(banner) > 0;
    }
}
