package com.wjb.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjb.shop.entity.Evaluation;
import com.wjb.shop.entity.Member;
import com.wjb.shop.entity.Shop;
import com.wjb.shop.mapper.EvaluationMapper;
import com.wjb.shop.mapper.MemberMapper;
import com.wjb.shop.mapper.ShopMapper;
import com.wjb.shop.service.EvaluationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class EvaluationServiceImpl implements EvaluationService {

    @Resource
    private EvaluationMapper evaluationMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private MemberMapper memberMapper;

    public List<Evaluation> selectByShopId(Long shopId) {
        Shop shop = shopMapper.selectById(shopId);
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper<Evaluation>();
        evaluationQueryWrapper.eq("shop_id", shopId);
        evaluationQueryWrapper.eq("state", "enable");
        evaluationQueryWrapper.orderByDesc("create_time");
        List<Evaluation> evaluations = evaluationMapper.selectList(evaluationQueryWrapper);
        for (Evaluation e:evaluations) {
            Member member = memberMapper.selectById(e.getMemberId());
            e.setShop(shop);
            e.setMember(member);
        }
        return evaluations;
    }

    public List<Evaluation> selectAll()  {
        List<Evaluation> evaluationList = evaluationMapper.selectList(new QueryWrapper<Evaluation>());
        return evaluationList;
    }

}
