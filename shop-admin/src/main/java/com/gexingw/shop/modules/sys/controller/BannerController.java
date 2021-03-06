package com.gexingw.shop.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.service.PmsBannerService;
import com.gexingw.shop.modules.sys.dto.banner.BannerRequestParam;
import com.gexingw.shop.modules.sys.dto.banner.BannerSearchParam;
import com.gexingw.shop.modules.sys.service.CommonService;
import com.gexingw.shop.modules.sys.service.UploadService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        // 分页
        IPage<PmsBanner> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        // 查询条件
        QueryWrapper<PmsBanner> queryWrapper = new QueryWrapper<>();

        // 是否启用
        if (searchParam.getBlurry() != null) {
            queryWrapper.like("name", searchParam.getBlurry());
        }

        // 开始时间
        if (searchParam.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", searchParam.getCreateTimeBegin());
        }

        // 结束时间
        if (searchParam.getCreateTimeBegin() != null) {
            queryWrapper.lt("create_time", searchParam.getCreateTimeEnd());
        }

        return R.ok(PageUtil.format(bannerService.search(queryWrapper, page)));
    }

    @PostMapping
    public R save(@RequestBody BannerRequestParam requestParam) {
        Long bannerId = bannerService.save(requestParam);
        if (bannerId == null) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        // TODO 删除已经无用的图片
        SysUpload upload = uploadService.attachPicToSource(bannerId, UploadConstant.UPLOAD_MODULE_BANNER, UploadConstant.UPLOAD_TYPE_IMAGE, requestParam.getPic());

        return upload != null ? R.ok(RespCode.BANNER_CREATED) : R.failure(RespCode.SAVE_FAILURE);
    }

    @PutMapping
    public R update(@RequestBody BannerRequestParam requestParam) {
        // 检查该banner是否存在
        PmsBanner banner = bannerService.findById(requestParam.getId());
        if (banner == null) {
            return R.failure(RespCode.BANNER_NOT_EXIST);
        }

        // 更新商品信息
        if (!bannerService.update(requestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // 更新图片
        if (!banner.getPic().equals(requestParam.getPic())) {
            // 删除旧图片
            uploadService.detachSourcePic(banner.getId(), UploadConstant.UPLOAD_MODULE_BANNER);

            // 更新新图片
            uploadService.attachPicToSource(banner.getId(), UploadConstant.UPLOAD_MODULE_BANNER, UploadConstant.UPLOAD_TYPE_IMAGE, requestParam.getPic());
        }

        return R.ok(RespCode.BANNER_UPDATED);
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        // TODO 删除已经无用的关联图片
        List<PmsBanner> banners = bannerService.getByIds(ids);
        if (!bannerService.deleteByIds(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        // 删除图片
        for (Long id : ids) {
            uploadService.detachSourcePic(id, UploadConstant.UPLOAD_MODULE_BANNER);
        }

        return R.ok(RespCode.BANNER_DELETED);
    }
}
