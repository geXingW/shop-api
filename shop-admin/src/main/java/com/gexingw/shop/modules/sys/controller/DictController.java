package com.gexingw.shop.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsDict;
import com.gexingw.shop.bo.ums.UmsDictDetail;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.ums.UmsDictMapper;
import com.gexingw.shop.modules.sys.dto.dict.UmsDictRequestParam;
import com.gexingw.shop.modules.sys.dto.dict.UmsDictSearchParam;
import com.gexingw.shop.modules.sys.service.UmsDictDetailService;
import com.gexingw.shop.modules.sys.service.UmsDictService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    UmsDictService umsDictService;

    @Autowired
    UmsDictDetailService umsDictDetailService;

    @Autowired
    UmsDictMapper umsDictMapper;

    @GetMapping
    @PreAuthorize("@el.check('job:list')")
    public R index(UmsDictSearchParam dictSearchParam) {
        // 分页
        IPage<UmsDict> page = new Page<>(dictSearchParam.getPage(), dictSearchParam.getSize());

        // 查询条件
        QueryWrapper<UmsDict> queryWrapper = new QueryWrapper<>();

        String blurry = dictSearchParam.getBlurry();
        if (StringUtils.hasText(blurry)) {
            queryWrapper.like("name", blurry).or().like("description", blurry);
        }

        return R.ok(PageUtil.format(umsDictMapper.selectPage(page, queryWrapper)));
    }

    @GetMapping("detail")
    @PreAuthorize("@el.check('job:edit')")
    R detail(@RequestParam("dictName") String dictName) {
        // 根据name查询dict ID
        UmsDict umsDict = umsDictService.findByName(dictName);

        ArrayList<UmsDictDetail> umsDictDetails = new ArrayList<UmsDictDetail>();
        if (umsDict == null) {
            return R.ok(umsDictDetails);
        }

        // 根据Id查询Detail信息
        List<UmsDictDetail> dictDetails = umsDictDetailService.getByDictId(umsDict.getId());

        return R.ok(dictDetails);
    }

    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public R save(@RequestBody UmsDictRequestParam requestParam) {
        return R.ok(umsDictService.save(requestParam), "已保存！");
    }

    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public R update(@RequestBody UmsDictRequestParam requestParam) {
        if (!umsDictService.update(requestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public R delete(@RequestBody List<Long> ids) {
        if (umsDictMapper.deleteBatchIds(ids) <= 0) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        if (!umsDictService.delete(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);

        }

        return R.ok("已删除！");
    }

}
