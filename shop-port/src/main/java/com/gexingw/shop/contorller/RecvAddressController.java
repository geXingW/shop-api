package com.gexingw.shop.contorller;

import com.gexingw.shop.bo.UmsMemberRecvAddress;
import com.gexingw.shop.dto.ums.UmsMemberRecvAddressRequestParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.CityService;
import com.gexingw.shop.service.UmsMemberRecvAddressService;
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

        // 设置postcode
        requestParam.setPostCode(cityService.findByName(requestParam.getRegion()));

        Long addressId = addressService.save(requestParam);
        return addressId != null ? R.ok(addressId, "已保存！") : R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
    }

    @PutMapping
    R update(@RequestBody UmsMemberRecvAddress requestParam) {
        // 设置收货地址的用户ID为当前登陆用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        // 设置postcode
        requestParam.setPostCode(cityService.findByName(requestParam.getRegion()));

        return addressService.update(requestParam) ? R.ok("已更新！") : R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
    }

    @DeleteMapping
    R delete(@RequestBody Set<Long> ids) {
        return addressService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
