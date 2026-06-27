package com.zhwl.demo.controller;


import cn.hutool.db.Page;
import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.BalanceUpdata;
import com.zhwl.demo.po.User;
import com.zhwl.demo.service.BalanceUpdataService;
import com.zhwl.demo.service.TxRecordService;
import com.zhwl.demo.service.UserService;
import com.zhwl.demo.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/txRecord")
@Api(tags = "提现模块")
@Slf4j
@Validated
public class TxRecordController {


    @Autowired
    private TxRecordService txRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceUpdataService updataService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/usertx")
    @ApiOperation("根据用户id和提现状态查询提现记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "状态 0全部 1提现中 2提现成功 3提现失败", name = "type",paramType = "query",dataType = "byte"),
    })
    public AjaxResult usertx(@RequestBody JSONObject jsonObject) {
        Integer userid = jsonObject.getInteger("userid");
        Byte type = jsonObject.getByte("type");
        return AjaxResult.success(txRecordService.selectByuserid(userid,type));
    }

    //分页查询所有提现订单
    @PostMapping("/selectAll")
    @ApiOperation("分页查询所有提现  0全部 1提现中 2提现成功 3提现失败")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码，0表示第一页", name = "pageNumber",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "每页结果数", name = "pageSize",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "状态 0全部 1提现中 2提现成功 3提现失败", name = "type",paramType = "query",dataType = "byte"),
    })
    public AjaxResult SelectAll(@RequestHeader("token") String token, @RequestBody JSONObject pageJson) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

         Integer pageNumber = pageJson.getInteger("pageNumber");
         Integer pageSize = pageJson.getInteger("pageSize");
         Byte type = pageJson.getByte("type");

        return AjaxResult.success(txRecordService.selectAll(pageNumber,pageSize,type));

    }


    @PostMapping("/createTx")
    @ApiOperation("用户申请提现")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "提现金额", name = "balance",paramType = "query",dataType = "BigDecimal"),
    })
    public AjaxResult createTx(@RequestBody JSONObject jsonObject) {
        Integer userid = jsonObject.getInteger("userid");
        BigDecimal balance = jsonObject.getBigDecimal("balance");
        //清空提现金额
        User user = userService.selectByid(userid);
        BigDecimal rebalance = user.getBalance().subtract(balance);
        if(rebalance.compareTo(BigDecimal.ZERO)<0){
            return AjaxResult.error("提现金额不能大于账户余额");
        }
        //更新提现金额
        userService.subBalance(rebalance,user.getOpenid());
        //生成余额变更记录
        Byte txByte = 3;
        updataService.Add(balance,txByte,"提现",userid);
        // 添加提现记录    提现中
        Byte type = 1;
        return AjaxResult.success(txRecordService.Add(balance,type,"提现中",userid));
    }



    @PostMapping("/updataType")
    @ApiOperation("修改提现状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "提现记录自增id", name = "id",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "2提现成功 3提现失败", name = "type",paramType = "query",dataType = "byte"),
    })
    public AjaxResult updataType(@RequestBody JSONObject jsonObject) {
        Integer userid = jsonObject.getInteger("userid");
        Integer id = jsonObject.getInteger("id");
        Byte type = jsonObject.getByte("type");
        String des = "";
        if(type==2){
            des = "提现成功";
        }else {
            des = "提现失败";
        }
        return AjaxResult.success(txRecordService.updataType(type,des,userid,id));

    }








}
