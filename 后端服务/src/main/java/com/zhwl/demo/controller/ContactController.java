package com.zhwl.demo.controller;

import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.mapper.UserMapper;
import com.zhwl.demo.po.User;
import com.zhwl.demo.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Author txq
 * @Date 2023/7/31 3:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/contact")
@Api(tags = "联系用户模块")
@Slf4j
@Validated
public class ContactController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    //前台查询是否有未读通知
    @PostMapping("/selectIsRead")
    @ApiOperation("根据用户id查询是否有未读通知，0是未读，1是已读")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult selectUserBlUpdata(@RequestBody User user) {

        User user1 = userMapper.selectByid(user.getUserid());
        HashMap isRead = new HashMap();
        isRead.put("isRead",user1.getIsRead());
        isRead.put("contact",user1.getContact());
        return AjaxResult.success(isRead);
    }




    @PostMapping("/userRead")
    @ApiOperation("根据用户id修改用户已读")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataIsRead(@RequestBody User user) {
        //情况用户已读
        userMapper.clear(user.getUserid());
        return AjaxResult.success(userMapper.updataIsRead(user.getUserid()));
    }


    @PostMapping("/contactUser")
    @ApiOperation("后台管理联系用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "联系内容", name = "contact",paramType = "query",dataType = "String"),
    })
    public AjaxResult contactUser(@RequestHeader("token") String token, @RequestBody User user) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        return AjaxResult.success(userMapper.contactUser(user.getUserid(),user.getContact()));
    }



}
