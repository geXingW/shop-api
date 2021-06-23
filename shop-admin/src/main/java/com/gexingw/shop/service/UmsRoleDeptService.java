package com.gexingw.shop.service;

import org.springframework.stereotype.Service;

import java.util.List;


public interface UmsRoleDeptService {

    List<Long> getDeptIdsByRoleIds(List<Long> roleIds);
}
