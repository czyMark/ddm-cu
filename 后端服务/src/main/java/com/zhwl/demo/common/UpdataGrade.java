package com.zhwl.demo.common;


import com.zhwl.demo.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdataGrade {

    @Autowired
    private GetWonService getWonService;

    @Scheduled(fixedDelay = 100000) // 每天0点0分执行
    public void clearClockinStatus() throws Exception {

        getWonService.updataWon();

        log.info("刷新订单状态成功");
    }







}
