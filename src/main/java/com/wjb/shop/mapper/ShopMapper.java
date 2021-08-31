package com.wjb.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjb.shop.entity.Shop;


public interface ShopMapper extends BaseMapper<Shop> {
    public void updateEvaluation();
}
