package com.wjb.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjb.shop.entity.Shop;

public interface ShopService {

    public IPage<Shop> paging(Long categoryId, String order, Integer page, Integer rows);

    public Shop selectById(Long shopId);

    public void updateEvaluation();

    public Shop createShop(Shop shop);

    public Shop updateShop(Shop shop);

    public void deleteShop(Long shopId);
}
