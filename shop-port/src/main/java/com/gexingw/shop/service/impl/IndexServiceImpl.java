package com.gexingw.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.enums.BannerShowStatusEnum;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.mapper.pms.PmsBannerMapper;
import com.gexingw.shop.service.IndexService;
import com.gexingw.shop.utils.PageUtil;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    PmsBannerMapper bannerMapper;

    @Override
    public List<PmsBanner> getBannerList() {
        QueryWrapper<PmsBanner> queryWrapper = new QueryWrapper<>();

        // 显示状态
        queryWrapper.eq("show_status", BannerShowStatusEnum.SHOW.getCode());

        // 有效期
        String now = DateUtil.now();
        queryWrapper.ge("start_time", now).lt("end_time", now);

        // 分页
        Page<PmsBanner> page = new Page<>(0, 10);

        return bannerMapper.selectPage(page, queryWrapper).getRecords();
    }
}
