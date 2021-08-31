package com.wjb.shop.task;

import com.wjb.shop.service.ShopService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ComputeTask {

    @Resource
    private ShopService shopService;

    @Scheduled(cron = "0 * * * * ?")
    public void updateEvaluation() {
        shopService.updateEvaluation();
    }
}
