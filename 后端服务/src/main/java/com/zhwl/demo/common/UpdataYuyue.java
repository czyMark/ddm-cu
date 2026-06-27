package com.zhwl.demo.common;


import com.zhwl.demo.service.YuyueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UpdataYuyue {


    @Autowired
    private YuyueService yuyueService;

    @Scheduled(fixedDelay = 120000) // 每天0点0分执行
    public void clearClockinStatus() throws Exception {

        yuyueService.yuyue();

        log.info("预约单刷新成功");
    }

}
