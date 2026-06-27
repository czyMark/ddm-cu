package com.zhwl.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @desc 启动类
 * @auther yxs
 * @date 2021/6/13 23:38
 */
@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = {"com.zhwl.demo.mapper"})
public class WebApplication {

    public static void main(String[] args){
        SpringApplication.run(WebApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ");

    }

}
