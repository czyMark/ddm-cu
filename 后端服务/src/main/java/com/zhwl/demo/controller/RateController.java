package com.zhwl.demo.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Jprate;
import com.zhwl.demo.po.MercariGoods;
import com.zhwl.demo.service.JprateService;
import com.zhwl.demo.service.RateService;
import com.zhwl.demo.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Author txq
 * @Date 2023/7/13 20:45
 * @Version 1.0
 */
@RestController
@RequestMapping("/rate")
@Api(tags = "中日汇率")
@Slf4j
@Validated
public class RateController {


    @Autowired
    private JprateService jprateService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("select")
    @ApiOperation("查询中日汇率")
    public AjaxResult selectrate() throws IOException {

        //Jprate jprate = jprateService.select();
        Jprate jprate = jprateService.select();
        return AjaxResult.success(jprate.getRate());

    }

    @PostMapping("updata")
    @ApiOperation("更新中日汇率")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "中日汇率", name = "rate",paramType = "query"),
    })
    public AjaxResult updataRate(@RequestHeader("token") String token, @RequestBody Jprate jprate) throws IOException {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        if(jprate.getRate()==null){
            return AjaxResult.error("汇率不能为空");
        }
        return AjaxResult.success(jprateService.updataSet(jprate.getRate()));
    }


}
