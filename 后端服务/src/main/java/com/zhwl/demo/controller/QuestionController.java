package com.zhwl.demo.controller;

import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Notice;
import com.zhwl.demo.po.Question;
import com.zhwl.demo.service.NoticeService;
import com.zhwl.demo.service.QuestionService;
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
@RequestMapping("/question")
@Api(tags = "常见问题列表")
@Slf4j
@Validated
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RedisUtils  redisUtils;

    @PostMapping("selectList")
    @ApiOperation("查询问题列表")
    public AjaxResult selectList() {
        return AjaxResult.success(questionService.select());
    }


    @PostMapping("selectByid")
    @ApiOperation("根据id查询问题")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "常见问题id", name = "id",paramType = "query"),
    })
    public AjaxResult selectByid(@RequestBody Question question) {
        return AjaxResult.success(questionService.selectByid(question.getId()));
    }

    @PostMapping("addQuestion")
    @ApiOperation("新增常见问题")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "常见问题标题", name = "title",paramType = "query"),
            @ApiImplicitParam(value = "常见问题内容", name = "content",paramType = "query"),
    })
    public AjaxResult addNotice(@RequestHeader("token") String token,@RequestBody Question question) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(questionService.Add(question.getTitle(),question.getContent()));
    }



    @PostMapping("updataQuestion")
    @ApiOperation("根据问题id修改标题和内容")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "常见问题标题", name = "title",paramType = "query"),
            @ApiImplicitParam(value = "常见问题内容", name = "content",paramType = "query"),
            @ApiImplicitParam(value = "常见问题id", name = "id",paramType = "query"),
    })
    public AjaxResult updataQuestion(@RequestHeader("token") String token,@RequestBody Question question) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(questionService.updata(question.getTitle(),question.getContent(),question.getId()));
    }



    @PostMapping("deleteQuestion")
    @ApiOperation("根据常见问题id删除")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "常见问题id", name = "id",paramType = "query"),
    })
    public AjaxResult deleteQuestion(@RequestHeader("token") String token,@RequestBody Question question) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(questionService.deleteByid(question.getId()));
    }

}
