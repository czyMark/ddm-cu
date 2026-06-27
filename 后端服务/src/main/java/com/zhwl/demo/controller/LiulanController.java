package com.zhwl.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.service.LiulanService;
import com.zhwl.demo.service.SMSClientService;
import com.zhwl.demo.service.SendMessageService;
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
import java.math.BigDecimal;

@RestController
@RequestMapping("/liulan")
@Api(tags = "浏览记录查询模块")
@Slf4j
@Validated
public class LiulanController {

    @Autowired
    private LiulanService liulanService;

    @Autowired
    private SMSClientService service;


    @PostMapping("cs")
    @ApiOperation("根据用户id和平台码查询浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "0雅虎，1煤炉", name = "platform",paramType = "query",dataType = "Int"),
    })
    public AjaxResult cs() throws Exception {
        return AjaxResult.success(service.sendMesssage("SS"));
    }


    @PostMapping("select")
    @ApiOperation("根据用户id和平台码查询浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "0雅虎，1煤炉", name = "platform",paramType = "query",dataType = "Int"),
    })
    public AjaxResult selectcookie(@RequestBody JSONObject jsonObject){
        Integer userid = jsonObject.getInteger("userid");
        Integer platform = jsonObject.getInteger("platform");
        return AjaxResult.success(liulanService.selectbyid(userid,platform));
    }

    @PostMapping("add")
    @ApiOperation("根据用户id和平台码新增浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "0雅虎，1煤炉", name = "platform",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品编码", name = "goodcode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品主图url地址", name = "imageUrl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "出价次数", name = "counts",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "现价", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult add(@RequestBody JSONObject jsonObject){
        Integer platform = jsonObject.getInteger("platform");
        String goodcode = jsonObject.getString("goodcode");
        String imageUrl = jsonObject.getString("imageUrl");
        String title = jsonObject.getString("title");
        String counts = jsonObject.getString("counts");
        BigDecimal price = jsonObject.getBigDecimal("price");
        Integer userid = jsonObject.getInteger("userid");
        return liulanService.add(platform,goodcode,imageUrl,title,counts,price,userid);
    }




}
