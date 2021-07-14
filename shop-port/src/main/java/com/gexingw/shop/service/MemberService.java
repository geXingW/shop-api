package com.gexingw.shop.service;

import com.gexingw.shop.bo.ums.UmsMember;

public interface MemberService {
    UmsMember getMemberDetailByMemberId(long memberId);

    UmsMember getRedisMemberDetailByMemberId(long memberId);

    boolean setRedisMemberDetailByMemberId(UmsMember umsMember);

    void delRedisMemberDetailByMemberId(long memberId);
}
