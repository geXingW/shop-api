package com.gexingw.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.constant.MemberConstant;
import com.gexingw.shop.mapper.UmsMemberMapper;
import com.gexingw.shop.service.MemberService;
import com.gexingw.shop.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsMemberMapper memberMapper;

    @Override
    public UmsMember getMemberDetailByMemberId(long memberId) {
        // 从Redis获取
        UmsMember umsMember = getRedisMemberDetailByMemberId(memberId);
        if (umsMember != null) {
            return umsMember;
        }

        // 从数据库查询
        umsMember = memberMapper.selectById(memberId);
        if(umsMember != null){
            setRedisMemberDetailByMemberId(umsMember);
        }

        return umsMember;
    }

    @Override
    public UmsMember getRedisMemberDetailByMemberId(long memberId) {
        Object redisObj = redisUtil.get(String.format(MemberConstant.REDIS_MEMBER_DETAIL_FORMAT, memberId));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(JSON.toJSONString(redisObj), UmsMember.class);
    }

    @Override
    public boolean setRedisMemberDetailByMemberId(UmsMember umsMember) {
        return redisUtil.set(String.format(MemberConstant.REDIS_MEMBER_DETAIL_FORMAT, umsMember.getId()), umsMember);
    }

    @Override
    public void delRedisMemberDetailByMemberId(long memberId) {
        redisUtil.del(String.format(MemberConstant.REDIS_MEMBER_DETAIL_FORMAT, memberId));
    }
}
