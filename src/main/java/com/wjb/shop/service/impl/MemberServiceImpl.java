package com.wjb.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjb.shop.entity.Evaluation;
import com.wjb.shop.entity.Member;
import com.wjb.shop.entity.MemberReadState;
import com.wjb.shop.exception.BussinessException;
import com.wjb.shop.mapper.EvaluationMapper;
import com.wjb.shop.mapper.MemberMapper;
import com.wjb.shop.mapper.MemberReadStateMapper;
import com.wjb.shop.service.MemberService;
import com.wjb.shop.util.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private MemberReadStateMapper memberReadStateMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    public Member createMember(String username, String password, String nickname) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username", username);
        List<Member> members = memberMapper.selectList(queryWrapper);
        if (members.size() > 0) {
            throw new BussinessException("M01", "用户名已存在");
        }
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
        int salt=new Random().nextInt(1000)+100;
        String newPassword = MD5Utils.md5Digest(password, salt);
        member.setSalt(salt);
        member.setPassword(newPassword);
        member.setCreateTime(new Date());
        memberMapper.insert(member);
        return member;
    }

    public Member memberLogin(String username, String password) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username", username);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member==null) {
            throw new BussinessException("M02", "用户不存在");
        }
        String s = MD5Utils.md5Digest(password, member.getSalt());
        if (!s.equals(member.getPassword())) {
            throw new BussinessException("M03", "密码错误");
        }
        return member;
    }
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public MemberReadState selectMemberReadState(Long memberId, Long shopId) {
        QueryWrapper<MemberReadState> memberReadStateQueryWrapper = new QueryWrapper<MemberReadState>();
        memberReadStateQueryWrapper.eq("shop_id", shopId);
        memberReadStateQueryWrapper.eq("member_id", memberId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(memberReadStateQueryWrapper);
        return memberReadState;
    }

    public MemberReadState updateMemberReadState(Long memberId, Long shopId, Integer readState) {
        QueryWrapper<MemberReadState> memberReadStateQueryWrapper = new QueryWrapper<MemberReadState>();
        memberReadStateQueryWrapper.eq("shop_id", shopId);
        memberReadStateQueryWrapper.eq("member_id", memberId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(memberReadStateQueryWrapper);
        if (memberReadState == null) {
            memberReadState = new MemberReadState();
            memberReadState.setMemberId(memberId);
            memberReadState.setShopId(shopId);
            memberReadState.setReadState(readState);
            memberReadState.setCreateTime(new Date());
            memberReadStateMapper.insert(memberReadState);
        } else {
            memberReadState.setReadState(readState);
            memberReadStateMapper.updateById(memberReadState);
        }
        return memberReadState;
    }

    public Evaluation evaluate(Long memberId, Long shopId, Integer score, String content) {
        Evaluation evaluation = new Evaluation();
        evaluation.setMemberId(memberId);
        evaluation.setShopId(shopId);
        evaluation.setScore(score);
        evaluation.setContent(content);
        evaluation.setCreateTime(new Date());
        evaluation.setState("enable");
        evaluation.setEnjoy(0);
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    public Evaluation enjoy(Long evaluationId) {
        Evaluation evaluation = evaluationMapper.selectById(evaluationId);
        evaluation.setEnjoy(evaluation.getEnjoy() + 1);
        evaluationMapper.updateById(evaluation);
        return evaluation;
    }
}
