package com.zhwl.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@Api(tags = "管理员登录模块")
@Slf4j
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("selectall")
    @ApiOperation("查询所有员工账号")
    public AjaxResult selectall(@RequestHeader("token") String token){
        return adminService.select(token);
    }


    @PostMapping("login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "账号", name = "username",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "密码", name = "password",paramType = "query",dataType = "String"),
    })
    public AjaxResult login(@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        return adminService.login(username,password);
    }

    @PostMapping("updatapassword")
    @ApiOperation("修改登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "账号", name = "username",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "原密码", name = "oldpassword",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "新密码", name = "newpassword",paramType = "query",dataType = "String"),
    })
    public AjaxResult updatapassword(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String oldpassword = jsonObject.getString("oldpassword");
        String newpassword =jsonObject.getString("newpassword");
        return adminService.updatapassword(username,oldpassword,newpassword,token);
    }



    @PostMapping("deleteByname")
    @ApiOperation("删除员工账号")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "账号", name = "username",paramType = "query",dataType = "String"),
    })
    public AjaxResult deleteByname(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        return adminService.deleteByname(username,token);
    }


    @PostMapping("add")
    @ApiOperation("新增员工账号")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "账号", name = "username",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "密码", name = "password",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "权限参数", name = "authtype",paramType = "query",dataType = "String"),
    })
    public AjaxResult add(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String authtype = jsonObject.getString("authtype");
        return adminService.add(username,password,authtype,token);
    }



}
