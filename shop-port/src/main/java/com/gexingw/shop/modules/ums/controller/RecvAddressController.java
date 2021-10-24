package com.gexingw.shop.modules.ums.controller;

import com.gexingw.shop.bo.ums.UmsMemberRecvAddress;
import com.gexingw.shop.bo.sys.SysCity;
import com.gexingw.shop.modules.ums.dto.UmsMemberRecvAddressRequestParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.CityService;
import com.gexingw.shop.modules.ums.service.UmsMemberRecvAddressService;
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

    @PostMapping
    R save(@RequestBody UmsMemberRecvAddressRequestParam requestParam) {
        // 设置收货地址的用户ID为当前登陆用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        // 根据省、市、区（县）确定唯一的postcode
        SysCity PCRCity = cityService.findByPCRNames(requestParam.getProvince(), requestParam.getCity(), requestParam.getRegion());

        // 设置postcode
        requestParam.setPostCode(PCRCity.getCode());

        Long addressId = addressService.save(requestParam);
        if (addressId == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
        }

        // 如果同时设置的默认选中，清除其他的选中
        if (requestParam.getDefaultStatus() == 1) {
            addressService.updateAddressDefaultStatusExcludeId(addressId);
        }

        return R.ok(addressId, "已保存！");
    }

    @PutMapping
    R update(@RequestBody UmsMemberRecvAddress requestParam) {
        // 设置收货地址的用户ID为当前登陆用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        // 根据省、市、区（县）确定唯一的postcode
        SysCity PCRCity = cityService.findByPCRNames(requestParam.getProvince(), requestParam.getCity(), requestParam.getRegion());

        // 设置postcode
        requestParam.setPostCode(PCRCity.getCode());
        if (!addressService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        if (requestParam.getDefaultStatus() == 1) {
            addressService.updateAddressDefaultStatusExcludeId(requestParam.getId());
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    R delete(@RequestBody Set<Long> ids) {
        return addressService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
