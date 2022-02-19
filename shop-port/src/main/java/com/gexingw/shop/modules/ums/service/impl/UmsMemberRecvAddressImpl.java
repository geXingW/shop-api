package com.gexingw.shop.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.ums.UmsMemberRecvAddress;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.ums.UmsMemberRecvAddressMapper;
import com.gexingw.shop.modules.ums.dto.UmsMemberRecvAddressRequestParam;
import com.gexingw.shop.modules.ums.service.UmsMemberRecvAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean update(UmsMemberRecvAddressRequestParam requestParam) {
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
        recvAddress.setDefaultStatus(UmsMemberRecvAddress.ADDRESS_NOT_DEFAULT);

        return addressMapper.update(recvAddress, new QueryWrapper<UmsMemberRecvAddress>().ne("id", id)) > 0;
    }

    @Override
    public UmsMemberRecvAddress getById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(UmsMemberRecvAddress address) {
        address.setDefaultStatus(UmsMemberRecvAddress.ADDRESS_DEFAULT);
        if (addressMapper.updateById(address) < 0) {
            throw new DBOperationException("设为默认收货地址失败！");
        }

        // 设置其他地址为非默认地址
        if (!this.updateAddressDefaultStatusExcludeId(address.getId())) {
            throw new DBOperationException("更新为非默认收获地址失败！");
        }

        return false;
    }

    @Override
    public boolean setFirstAddressAsDefault() {
        UmsMemberRecvAddress address = addressMapper.selectOne(new QueryWrapper<UmsMemberRecvAddress>().orderByAsc("id"));
        address.setDefaultStatus(UmsMemberRecvAddress.ADDRESS_DEFAULT);

        return addressMapper.updateById(address) >= 0;
    }

    @Override
    public UmsMemberRecvAddress getDefaultAddress() {
        QueryWrapper<UmsMemberRecvAddress> queryWrapper = new QueryWrapper<UmsMemberRecvAddress>().eq("default_status", UmsMemberRecvAddress.ADDRESS_DEFAULT);
        return addressMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean deleteByIds(Set<Long> ids) {
        return addressMapper.deleteBatchIds(ids) > 0;
    }
}
