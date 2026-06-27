package com.zhwl.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.service.SMSClientService;
import com.zhwl.demo.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/phone")
@Api(tags = "手机验证码模块")
@Slf4j
@Validated
public class PhoneController {

    @Autowired
    private SMSClientService smsClientService;


    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("getcode")
    @ApiOperation("获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone",paramType = "query",dataType = "String"),
    })
    public AjaxResult addNotice(@RequestBody JSONObject jsonObject) throws Exception {
        return  AjaxResult.success(smsClientService.sendMesssage(jsonObject.getString("phone")));

    }

    @PostMapping("checkcode")
    @ApiOperation("检测验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "验证码", name = "code",paramType = "query",dataType = "String"),
    })
    public AjaxResult checkcode(@RequestBody JSONObject jsonObject) throws Exception {
        String phone = jsonObject.getString("phone");
        String code = jsonObject.getString("code");
        if(redisUtils.hasKey(phone)){
            String codere = (String) redisUtils.valueGet(phone);
            if(codere.equals(code)){
                return  AjaxResult.success("验证成功");
            }else {
                return  AjaxResult.error("验证码错误");
            }
        }else {
            return  AjaxResult.error("验证码错误");
        }

    }

}
