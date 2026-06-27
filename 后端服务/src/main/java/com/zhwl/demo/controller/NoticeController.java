package com.zhwl.demo.controller;

import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Notice;
import com.zhwl.demo.service.NoticeService;
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
 * @Date 2023/7/9 15:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/notice")
@Api(tags = "公告模块")
@Slf4j
@Validated
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("selectList")
    @ApiOperation("查询公告列表")
    public AjaxResult selectList() {
        return AjaxResult.success(noticeService.select());
    }


    @PostMapping("selectByid")
    @ApiOperation("根据id查询公告")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公告内容id", name = "id",paramType = "query"),
    })
    public AjaxResult selectByid(@RequestBody Notice notice) {
        return AjaxResult.success(noticeService.selectByid(notice.getId()));
    }


    @PostMapping("addNotice")
    @ApiOperation("新增公告")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公告标题", name = "title",paramType = "query"),
            @ApiImplicitParam(value = "公告内容", name = "content",paramType = "query"),
    })
    public AjaxResult addNotice(@RequestHeader("token") String token,@RequestBody Notice notice) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(noticeService.Add(notice.getTitle(),notice.getContent()));
    }



    @PostMapping("updataNotice")
    @ApiOperation("根据公告id修改标题和内容")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公告标题", name = "title",paramType = "query"),
            @ApiImplicitParam(value = "公告内容", name = "content",paramType = "query"),
            @ApiImplicitParam(value = "公告内容id", name = "id",paramType = "query"),
    })
    public AjaxResult updataNotice(@RequestHeader("token") String token,@RequestBody Notice notice) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        return AjaxResult.success(noticeService.updata(notice.getTitle(),notice.getContent(),notice.getId()));
    }



    @PostMapping("deleteNotice")
    @ApiOperation("根据公告id删除公告")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公告内容id", name = "id",paramType = "query"),
    })
    public AjaxResult deleteNotice(@RequestHeader("token") String token,@RequestBody Notice notice) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        return AjaxResult.success(noticeService.deleteByid(notice.getId()));
    }

}
