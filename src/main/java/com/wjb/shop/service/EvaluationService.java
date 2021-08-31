package com.wjb.shop.service;

import com.wjb.shop.entity.Evaluation;

import java.util.List;

public interface EvaluationService {
    public List<Evaluation> selectByShopId(Long shopId);

    public List<Evaluation> selectAll();
}
