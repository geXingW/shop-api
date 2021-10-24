package com.gexingw.shop.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.ums.UmsDict;
import com.gexingw.shop.bo.ums.UmsDictDetail;
import com.gexingw.shop.modules.sys.dto.dict.UmsDictRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.ums.UmsDictDetailMapper;
import com.gexingw.shop.mapper.ums.UmsDictMapper;
import com.gexingw.shop.modules.sys.service.UmsDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UmsDictServiceImpl implements UmsDictService {

    @Autowired
    private UmsDictMapper umsDictMapper;

    @Autowired
    private UmsDictDetailMapper umsDictDetailMapper;

    @Override
    public UmsDict findByName(String name) {
        return umsDictMapper.findByName(name);
    }

    @Override
    public Long save(UmsDictRequestParam requestParam) {
        UmsDict umsDict = new UmsDict();
        BeanUtils.copyProperties(requestParam, umsDict);
        if (umsDictMapper.insert(umsDict) <= 0) {
            throw new DBOperationException("保存失败！");
        }

        return umsDict.getId();
    }

    @Override
    public boolean update(UmsDictRequestParam requestParam) {
        // 检查当前字典是否存在
        UmsDict umsDict = umsDictMapper.selectById(requestParam.getId());
        if (umsDict == null) {
            throw new ResourceNotExistException("字典不存在！");
        }

        BeanUtils.copyProperties(requestParam, umsDict);
        if (umsDictMapper.updateById(umsDict) <= 0) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public boolean delete(List<Long> ids) {
        // 删除该字典下的字典详情
        if (umsDictDetailMapper.delete(new QueryWrapper<UmsDictDetail>().in("dict_id", ids)) <= 0) {
            throw new ResourceNotExistException("字典详情删除失败！");
        }

        return umsDictMapper.deleteBatchIds(ids) > 0;
    }
}
