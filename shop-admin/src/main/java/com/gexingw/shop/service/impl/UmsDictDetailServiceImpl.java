package com.gexingw.shop.service.impl;

import com.gexingw.shop.bean.ums.UmsDictDetail;
import com.gexingw.shop.dto.dict.UmsDictDetailRequestParam;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.UmsDictDetailMapper;
import com.gexingw.shop.service.UmsDictDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsDictDetailServiceImpl implements UmsDictDetailService {

    @Autowired
    private UmsDictDetailMapper umsDictDetailMapper;

    @Override
    public List<UmsDictDetail> getByDictId(long dictId) {
        return umsDictDetailMapper.getByDictId(dictId);
    }

    @Override
    public Long save(UmsDictDetailRequestParam dictDetailRequestParam) {
        UmsDictDetail dictDetail = new UmsDictDetail();
        BeanUtils.copyProperties(dictDetailRequestParam, dictDetail, "dictId");
        dictDetail.setDictId(dictDetailRequestParam.getDict().getId());

        if (umsDictDetailMapper.insert(dictDetail) <= 0) {
            throw new RuntimeException("保存失败！");
        }

        return dictDetail.getId();
    }

    @Override
    public boolean update(UmsDictDetailRequestParam dictDetailRequestParam) {
        UmsDictDetail dictDetail = umsDictDetailMapper.selectById(dictDetailRequestParam.getId());
        if (dictDetail == null) {
            throw new ResourceNotExistException("更新失败，字典详情不存在！");
        }

        BeanUtils.copyProperties(dictDetailRequestParam, dictDetail);
        return umsDictDetailMapper.updateById(dictDetail) > 0;
    }

}
