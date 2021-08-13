package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.UmsMemberRecvAddress;
import com.gexingw.shop.dto.ums.UmsMemberRecvAddressRequestParam;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.ums.UmsMemberRecvAddressMapper;
import com.gexingw.shop.service.UmsMemberRecvAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UmsMemberRecvAddressImpl implements UmsMemberRecvAddressService {

    @Autowired
    UmsMemberRecvAddressMapper addressMapper;

    @Override
    public List<UmsMemberRecvAddress> getListByMemberId(long memberId) {
        QueryWrapper<UmsMemberRecvAddress> queryWrapper = new QueryWrapper<UmsMemberRecvAddress>()
                .eq("member_id", memberId).orderByDesc("default_status");
        return addressMapper.selectList(queryWrapper);
    }

    @Override
    public Long save(UmsMemberRecvAddressRequestParam requestParam) {
        UmsMemberRecvAddress address = new UmsMemberRecvAddress();

        BeanUtils.copyProperties(requestParam, address);

        if (addressMapper.insert(address) <= 0) {
            return null;
        }

        return address.getId();
    }

    @Override
    public boolean update(UmsMemberRecvAddress requestParam) {
        UmsMemberRecvAddress address = addressMapper.selectById(requestParam.getId());
        if (address == null) {
            throw new ResourceNotExistException("地址不存在！");
        }

        BeanUtils.copyProperties(requestParam, address);

        return addressMapper.updateById(address) > 0;
    }

    /**
     * 将不等于该 `id` 的记录 `default_status` 更新为0
     *
     * @param id
     * @return
     */
    @Override
    public boolean updateAddressDefaultStatusExcludeId(Long id) {
        UmsMemberRecvAddress recvAddress = new UmsMemberRecvAddress();
        recvAddress.setDefaultStatus(0);

        return addressMapper.update(recvAddress, new QueryWrapper<UmsMemberRecvAddress>().ne("id", id)) > 0;
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        return addressMapper.deleteBatchIds(ids) > 0;
    }
}
