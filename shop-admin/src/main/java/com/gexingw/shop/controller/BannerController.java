package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.sys.SysUpload;
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
        IPage<PmsBanner> page = new Page<PmsBanner>(searchParam.getPage(), searchParam.getSize());

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
        if(searchParam.getCreateTimeBegin() != null){
            queryWrapper.lt("create_time", searchParam.getCreateTimeEnd());
        }

        return R.ok(PageUtil.format(bannerService.search(queryWrapper, page)));
    }

    @PostMapping
    public R save(@RequestBody BannerRequestParam requestParam) {
        Long bannerId = bannerService.save(requestParam);
        if (bannerId == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
        }

        // TODO 删除已经无用的图片
        SysUpload upload = uploadService.attachPicToSource(bannerId, UploadConstant.UPLOAD_TYPE_BANNER, requestParam.getPic());

        return upload != null ? R.ok("已添加！") : R.ok(RespCode.FAILURE.getCode(), "添加失败！");
    }

    @PutMapping
    public R update(@RequestBody BannerRequestParam requestParam) {
        // 检查该banner是否存在
        PmsBanner banner = bannerService.findById(requestParam.getId());
        if (banner == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "未找到该Banner！");
        }

        // 更新商品信息
        if (!bannerService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        // 更新图片
        if (!banner.getPic().equals(requestParam.getPic())) {
            // 删除旧图片
            uploadService.detachSourcePic(banner.getId(), UploadConstant.UPLOAD_TYPE_BANNER);

            // 更新新图片
            uploadService.attachPicToSource(banner.getId(), UploadConstant.UPLOAD_TYPE_BANNER, requestParam.getPic());
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        // TODO 删除已经无用的关联图片
        List<PmsBanner> banners = bannerService.getByIds(ids);
        if (!bannerService.deleteByIds(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        // 删除图片
        for (Long id : ids) {
            uploadService.detachSourcePic(id, UploadConstant.UPLOAD_TYPE_BANNER);
        }

        return R.ok("已删除！");
    }
}
