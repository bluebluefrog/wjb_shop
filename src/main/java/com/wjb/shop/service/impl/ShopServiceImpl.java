package com.wjb.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjb.shop.entity.Evaluation;
import com.wjb.shop.entity.MemberReadState;
import com.wjb.shop.entity.Shop;
import com.wjb.shop.mapper.EvaluationMapper;
import com.wjb.shop.mapper.MemberReadStateMapper;
import com.wjb.shop.mapper.ShopMapper;
import com.wjb.shop.service.ShopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private MemberReadStateMapper memberReadStateMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    public IPage<Shop> paging(Long categoryId, String order, Integer page, Integer rows) {
        Page<Shop> p = new Page<Shop>(page,rows);
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<Shop>();
        if (categoryId != null && categoryId != -1) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (order != null) {
            if(order.equals("quantity")){
                queryWrapper.orderByDesc("evaluation_quantity");
            }else if(order.equals("score")){
                queryWrapper.orderByDesc("evaluation_score");
            }
        }
        Page<Shop> shopPage = shopMapper.selectPage(p, queryWrapper);
        return shopPage;
    }

    public Shop selectById(Long shopId) {
        Shop shop = shopMapper.selectById(shopId);
        return shop;
    }

    @Transactional
    public void updateEvaluation() {
        shopMapper.updateEvaluation();
    }

    @Transactional
    public Shop createShop(Shop shop) {
        shopMapper.insert(shop);
        return shop;
    }

    @Transactional
    public Shop updateShop(Shop shop) {
        shopMapper.updateById(shop);
        return shop;
    }

    @Transactional
    public void deleteShop(Long shopId) {
        shopMapper.deleteById(shopId);

        QueryWrapper<MemberReadState> mrs = new QueryWrapper<MemberReadState>();
        mrs.eq("shop_id", shopId);
        memberReadStateMapper.delete(mrs);

        QueryWrapper<Evaluation> eva = new QueryWrapper<Evaluation>();
        eva.eq("shop_id", shopId);
        evaluationMapper.delete(eva);
    }
}
