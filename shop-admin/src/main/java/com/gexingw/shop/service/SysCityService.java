package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.dto.city.SysCityRequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysCityService {
    boolean deleteByIds(Set<Integer> ids);

    IPage<SysCity> searchList(QueryWrapper<SysCity> queryWrapper, IPage<SysCity> page);

    Integer save(SysCityRequestParam requestParam);

    boolean update(SysCityRequestParam requestParam);

    List<SysCity> getListByParentCode(Integer code);

    SysCity findById(Long id);

    List<Map<String, Object>> getPeerAndParentListByParentCode(Integer parentCode, Integer targetParentCode);
}
