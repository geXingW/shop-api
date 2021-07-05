package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.ums.UmsDict;
import com.gexingw.shop.bo.ums.UmsDictDetail;
import com.gexingw.shop.dto.dict.UmsDictDetailRequestParam;
import com.gexingw.shop.dto.dict.UmsDictDetailSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.UmsDictDetailMapper;
import com.gexingw.shop.mapper.UmsDictMapper;
import com.gexingw.shop.service.UmsDictDetailService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dictDetail")
public class DictDetailController {

    @Autowired
    UmsDictDetailMapper umsDictDetailMapper;

    @Autowired
    UmsDictMapper umsDictMapper;

    @Autowired
    UmsDictDetailService umsDictDetailService;

    @GetMapping
    public R index(UmsDictDetailSearchParam requestParam) {
        // 分页
        IPage<UmsDictDetail> page = new Page<>(requestParam.getPage(), requestParam.getSize());

        // 查询条件
        QueryWrapper<UmsDictDetail> queryWrapper = new QueryWrapper<UmsDictDetail>();

        String dictName = requestParam.getDictName();
        if (StringUtils.hasText(dictName)) {
            // 查询dict详情
            UmsDict umsDict = umsDictMapper.selectOne(new QueryWrapper<UmsDict>().eq("name", dictName));
            queryWrapper.eq("dict_id", umsDict.getId());
        }

        return R.ok(PageUtil.format(umsDictDetailMapper.selectPage(page, queryWrapper)));
    }

    @PostMapping
    public R save(@RequestBody UmsDictDetailRequestParam dictDetailRequestParam) {
        return R.ok(umsDictDetailService.save(dictDetailRequestParam), "已保存！");
    }

    @PutMapping
    public R update(@RequestBody UmsDictDetailRequestParam dictDetailRequestParam) {
        if (!umsDictDetailService.update(dictDetailRequestParam)) {
            return R.ok("更新失败！");
        }

        return R.ok("已更新！");
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        if (umsDictDetailMapper.deleteById(id) <= 0) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }
        return R.ok("已删除！");
    }
}
