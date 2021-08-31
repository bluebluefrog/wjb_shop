package com.wjb.shop.service;

import com.wjb.shop.entity.Evaluation;
import com.wjb.shop.entity.Member;
import com.wjb.shop.entity.MemberReadState;

public interface MemberService {

    public Member createMember(String username, String password, String nickname);

    public Member memberLogin(String username, String password);

    public MemberReadState selectMemberReadState(Long memberId, Long shopId);

    public MemberReadState updateMemberReadState(Long memberId, Long shopId, Integer readState);

    public Evaluation evaluate(Long memberId, Long shopId, Integer score, String content);

    public Evaluation enjoy(Long evaluationId);
}
