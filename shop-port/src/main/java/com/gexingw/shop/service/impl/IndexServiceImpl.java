package com.gexingw.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.enums.BannerShowStatusEnum;
import com.gexingw.shop.enums.PmsProductIsNewEnum;
import com.gexingw.shop.enums.PmsProductIsRecommendEnum;
import com.gexingw.shop.enums.PmsProductOnSaleEnum;
import com.gexingw.shop.mapper.pms.PmsBannerMapper;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    PmsBannerMapper bannerMapper;

    @Autowired
    PmsProductMapper productMapper;

    /**
     * 首页轮播图
     *
     * @return
     */
    @Override
    public List<PmsBanner> getBannerList() {
        QueryWrapper<PmsBanner> queryWrapper = new QueryWrapper<>();

        // 显示状态
        queryWrapper.eq("show_status", BannerShowStatusEnum.SHOW.getCode());

        // 有效期
        String now = DateUtil.now();
        queryWrapper.le("start_time", now).gt("end_time", now);

        // 分页
        Page<PmsBanner> page = new Page<>(1, 10);

        return bannerMapper.selectPage(page, queryWrapper).getRecords();
    }

    /**
     * 热门商品
     *
     * @return
     */
    @Override
    public List<PmsProduct> getHotProduct() {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<PmsProduct>().orderByDesc("sale");

        // 已上架
        queryWrapper.eq("on_sale", PmsProductOnSaleEnum.ON_SALE.getCode());

        // 分页
        Page<PmsProduct> page = new Page<>(1, 8);

        return productMapper.selectPage(page, queryWrapper).getRecords();
    }

    /**
     * 精品推荐
     *
     * @return
     */
    @Override
    public List<PmsProduct> getRecommendProduct() {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        // 推荐状态
        queryWrapper.eq("on_sale", PmsProductOnSaleEnum.ON_SALE.getCode())
                .eq("is_recommend", PmsProductIsRecommendEnum.RECOMMEND.getCode());

        // 分页
        Page<PmsProduct> page = new Page<>(1, 7);

        return productMapper.selectPage(page, queryWrapper).getRecords();
    }

    /**
     * 新品推荐
     *
     * @return
     */
    @Override
    public List<PmsProduct> getNewProduct() {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        // 推荐状态
        queryWrapper.eq("on_sale", PmsProductOnSaleEnum.ON_SALE.getCode())
                .eq("is_new", PmsProductIsNewEnum.NEW.getCode());

        // 分页
        Page<PmsProduct> page = new Page<>(1, 10);

        return productMapper.selectPage(page, queryWrapper).getRecords();
    }
}
