package com.gexingw.shop.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.mapper.ums.UmsMemberMapper;
import com.gexingw.shop.modules.ums.service.UmsMemberService;
import org.springframework.stereotype.Service;

@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {

}
