package com.woowahan.moduchan.support;

import com.woowahan.moduchan.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Scheduled(cron="0 0/5 * * * *")
    public void updatePendingOrderHistory(){
        orderHistoryService.updatePendingOrderHistory();
    }
}
