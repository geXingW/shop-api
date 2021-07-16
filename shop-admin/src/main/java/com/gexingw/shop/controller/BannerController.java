package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.Upload;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.dto.banner.BannerRequestParam;
import com.gexingw.shop.dto.banner.BannerSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.CommonService;
import com.gexingw.shop.service.PmsBannerService;
import com.gexingw.shop.service.UploadService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("banner")
public class BannerController {

    @Autowired
    PmsBannerService bannerService;

    @Autowired
    CommonService commonService;

    @Autowired
    UploadService uploadService;

    @GetMapping
    public R index(BannerSearchParam searchParam) {
        IPage<PmsBanner> page = new Page<PmsBanner>(searchParam.getPage(), searchParam.getSize());
        QueryWrapper<PmsBanner> queryWrapper = new QueryWrapper<>();

        // 是否启用
//        if(searchParam.){
//
//        }

        return R.ok(PageUtil.format(bannerService.search(queryWrapper, page)));
    }

    @PostMapping
    public R save(@RequestBody BannerRequestParam requestParam) {
        Long bannerId = bannerService.save(requestParam);
        if (bannerId == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
        }

        // TODO 删除已经无用的图片
        Upload upload = uploadService.attachPicToSource(bannerId, UploadConstant.UPLOAD_TYPE_BANNER, requestParam.getPic());

        return upload != null ? R.ok("已添加！") : R.ok(RespCode.FAILURE.getCode(), "添加失败！");
    }

    @PutMapping
    public R update(BannerRequestParam requestParam) {
        // TODO 删除已经无用的关联图片

        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(Set<Long> ids) {
        // TODO 删除已经无用的关联图片

        return R.ok("已删除！");
    }
}
