package com.gexingw.shop.modules.ums.service;

import java.util.List;


public interface UmsRoleDeptService {

    List<Long> getDeptIdsByRoleIds(List<Long> roleIds);
}
