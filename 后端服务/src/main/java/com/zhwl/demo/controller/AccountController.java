package com.zhwl.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Account;
import com.zhwl.demo.service.AccountService;
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

/**
 * @Author txq
 * @Date 2023/7/9 18:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/cookie")
@Api(tags = "cookie账号模块")
@Slf4j
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("select")
    @ApiOperation("查询cookie")
    public AjaxResult selectcookie() throws IOException {
        return AjaxResult.success(accountService.select());
    }


    @PostMapping("updata")
    @ApiOperation("根据id修改cookie")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "雅虎cookie状态", name = "cookie",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult updatacookie(@RequestBody Account cookie) throws IOException {
        return AjaxResult.success(accountService.updataSet(cookie.getCookie(),cookie.getId()));
    }





}
