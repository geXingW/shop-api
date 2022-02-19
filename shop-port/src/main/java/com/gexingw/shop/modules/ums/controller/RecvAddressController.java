package com.gexingw.shop.modules.ums.controller;

import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.bo.ums.UmsMemberRecvAddress;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.exception.BadRequestException;
import com.gexingw.shop.modules.ums.dto.UmsMemberRecvAddressRequestParam;
import com.gexingw.shop.modules.ums.service.UmsMemberRecvAddressService;
import com.gexingw.shop.service.CityService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("recv-address")
public class RecvAddressController {

    @Autowired
    UmsMemberRecvAddressService addressService;

    @Autowired
    CityService cityService;

    @GetMapping
    R index() {
        return R.ok(addressService.getListByMemberId(AuthUtil.getAuthId()));
    }

    @GetMapping("city-tree")
    R cityTree() {
        return R.ok(cityService.getCityTree());
    }

    @GetMapping("default")
    public R defaultAddress() {
        return R.ok(addressService.getDefaultAddress());
    }

    @PostMapping
    R save(@RequestBody UmsMemberRecvAddressRequestParam requestParam) {
        // 设置收货地址的用户ID为当前登陆用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        try {
            this.setAreaNamesToRequest(requestParam);
        } catch (BadRequestException e) {
            return R.ok(e.getCode(), e.getMessage());
        }

        // 如果用户没有填写邮政编码，默认为区县的编码
        if (requestParam.getPostCode() == null) {
            requestParam.setPostCode(requestParam.getRegionCode());
        }

        Long addressId = addressService.save(requestParam);

        // 如果同时设置的默认选中，清除其他的选中
        if (UmsMemberRecvAddress.ADDRESS_DEFAULT.equals(requestParam.getDefaultStatus())) {
            addressService.updateAddressDefaultStatusExcludeId(addressId);
        }

        return R.ok(addressId, "已保存！");
    }

    @PutMapping("/{id}")
    R update(@PathVariable Long id, @RequestBody UmsMemberRecvAddressRequestParam requestParam) {
        // 设置收货地址的用户ID为当前登陆用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        UmsMemberRecvAddress address = addressService.getById(id);
        if (address == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "未找到该收货地址！");
        }

        try {
            this.setAreaNamesToRequest(requestParam);
        } catch (BadRequestException e) {
            return R.ok(e.getCode(), e.getMessage());
        }

        // 如果用户没有填写邮政编码，默认为区县的编码
        if (requestParam.getPostCode() == null) {
            requestParam.setPostCode(requestParam.getRegionCode());
        }

        if (!addressService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        // 如果该地址为默认收货地址，那么其他地址应设为非默认收货地址
        if (UmsMemberRecvAddress.ADDRESS_DEFAULT.equals(requestParam.getDefaultStatus())) {
            addressService.updateAddressDefaultStatusExcludeId(requestParam.getId());
        }

        return R.ok("已更新！");
    }

    @PutMapping("/{id}/set-default")
    public R setDefault(@PathVariable Long id) {
        UmsMemberRecvAddress address = addressService.getById(id);
        if (address == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "收货地址不存在！");
        }

        if (addressService.setDefault(address)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    R delete(@RequestBody Set<Long> ids) {
        if (!addressService.deleteByIds(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        // 设置第一个收货地址为默认收货地址
        if (addressService.setFirstAddressAsDefault()) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "设置默认收货地址失败！");
        }

        return R.ok("已删除！");
    }

    private void setAreaNamesToRequest(UmsMemberRecvAddressRequestParam requestParam) {

        // 根据省份编码，查询省份信息
        SysCity province = cityService.findByCode(requestParam.getProvinceCode());
        if (province == null) {
            throw new BadRequestException(RespCode.RESOURCE_NOT_EXIST.getCode(), "省份信息未找到！");
        }

        // 根据城市编码，查询城市信息
        SysCity city = cityService.findByCode(requestParam.getCityCode());
        if (city == null) {
            throw new BadRequestException(RespCode.RESOURCE_NOT_EXIST.getCode(), "城市信息未找到！");
        }

        // 根据区县编码，查询区县信息
        SysCity region = cityService.findByCode(requestParam.getRegionCode());
        if (region == null) {
            throw new BadRequestException(RespCode.RESOURCE_NOT_EXIST.getCode(), "区县信息未找到！");
        }

        requestParam.setProvinceName(province.getName()); // 省份
        requestParam.setCityName(city.getName());   // 城市
        requestParam.setRegionName(region.getName());   // 区县
    }
}
