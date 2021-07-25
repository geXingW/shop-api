package com.gexingw.shop.contorller;

import com.gexingw.shop.bo.UmsMemberRecvAddress;
import com.gexingw.shop.enums.RespCode;
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

    @GetMapping
    R index() {
        return R.ok(addressService.getListByMemberId(AuthUtil.getAuthId()));
    }

    @PostMapping
    R save(UmsMemberRecvAddress requestParam) {
        Long addressId = addressService.save(requestParam);
        return addressId != null ? R.ok(addressId, "已保存！") : R.ok(RespCode.SAVE_FAILURE.getCode(), "保存失败！");
    }

    @PutMapping
    R update(UmsMemberRecvAddress requestParam) {
        return addressService.update(requestParam) ? R.ok("已更新！") : R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
    }

    @DeleteMapping
    R delete(Set<Long> ids) {
        return addressService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
