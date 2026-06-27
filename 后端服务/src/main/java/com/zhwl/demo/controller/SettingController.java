package com.zhwl.demo.controller;

import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Question;
import com.zhwl.demo.po.Settings;
import com.zhwl.demo.service.SettingService;
import com.zhwl.demo.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author txq
 * @Date 2023/7/9 16:42
 * @Version 1.0
 */

@RestController
@RequestMapping("/setting")
@Api(tags = "帮助设置")
@Slf4j
@Validated
public class SettingController {


    @Autowired
    private SettingService settingService;

    @Autowired
    private RedisUtils redisUtils;


    @PostMapping("updataSetting")
    @ApiOperation("修改帮助设置")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "客服联系方式", name = "customer",paramType = "query"),
            @ApiImplicitParam(value = "用户协议", name = "usergreement",paramType = "query"),
            @ApiImplicitParam(value = "竞拍规则", name = "biddingrules",paramType = "query"),
            @ApiImplicitParam(value = "隐私协议", name = "privacy",paramType = "query"),
            @ApiImplicitParam(value = "出价规则文案", name = "chujiarules",paramType = "query"),
            @ApiImplicitParam(value = "风险提示文案", name = "riskwarning",paramType = "query"),
    })
    public AjaxResult updataSetting(@RequestHeader("token") String token, @RequestBody Settings settings) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(settingService.updata(settings.getCustomer(),settings.getUsergreement(),settings.getBiddingrules(),settings.getPrivacy(),settings.getChujiarules(),settings.getRiskwarning()));
    }


    @PostMapping("select")
    @ApiOperation("查询帮助设置")
    public AjaxResult select() {
        return AjaxResult.success(settingService.select());
    }





}
