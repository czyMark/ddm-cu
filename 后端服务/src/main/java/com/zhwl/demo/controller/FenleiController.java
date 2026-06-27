package com.zhwl.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.mapper.FenleiMapper;
import com.zhwl.demo.po.Fenlei;
import com.zhwl.demo.po.User;
import com.zhwl.demo.utils.RedisUtils;
import com.zhwl.ruoyi.common.utils.uuid.UUID;
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

import java.util.Date;
import java.util.HashMap;


@RestController
@RequestMapping("/fenlei")
@Api(tags = "主页分类")
@Slf4j
@Validated
public class FenleiController {

    @Autowired
    private FenleiMapper fenleiMapper;

    @Autowired
    private RedisUtils redisUtils;

    //前台查询是否有未读通知
    @PostMapping("/select")
    @ApiOperation("查询分类json字符串")
    public AjaxResult selectUserBlUpdata() {
        log.info("方法开始之前"+String.valueOf(System.currentTimeMillis()));
        Fenlei fenlei = fenleiMapper.select();
        log.info(String.valueOf("数据库查询结束返回"+System.currentTimeMillis()));
        return AjaxResult.success(fenlei);
    }

    @PostMapping("/updata")
    @ApiOperation("修改分类json字符串")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "字符串内容", name = "content",paramType = "query",dataType = "String"),
    })
    public AjaxResult updataIsRead(@RequestBody JSONObject jsonObject) {
        //情况用户已读
        String content = jsonObject.getString("content");
        log.info(content);
        redisUtils.delKey("fljson");

        long time = 0;
        redisUtils.valueSet("fljson",content,time);

        return AjaxResult.success(fenleiMapper.updata(content));
    }



}
