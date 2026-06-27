package com.zhwl.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.config.RuoYiConfig;
import com.zhwl.demo.config.ServerConfig;
import com.zhwl.demo.po.Image;
import com.zhwl.demo.service.ImageService;
import com.zhwl.demo.utils.FileUploadUtils;
import com.zhwl.demo.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author txq
 * @Date 2023/7/13 21:31
 * @Version 1.0
 */


@RestController
@RequestMapping("/image")
@Api(tags = "轮播图模块")
@Slf4j
@Validated
public class ImageController {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ImageService imageService;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 通用上传请求
     */
    @PostMapping("add")
    @ResponseBody
    @ApiOperation("新增轮播图")
    public AjaxResult uploadImage(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);

            String url =  fileName;
            url=url.substring(8);


            return AjaxResult.success("上传成功",imageService.add(url));
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }



    @PostMapping("/selectAll")
    @ApiOperation("查询轮播图列表")
    public AjaxResult selectAll() {
        return AjaxResult.success(imageService.select());
    }


    @PostMapping("updata")
    @ResponseBody
    @ApiOperation("根据图片id修改轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "轮播图片id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataImage(MultipartFile file,Integer id) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);

            String url =  fileName;
            url=url.substring(8);


            return AjaxResult.success("更新成功",imageService.updata(url,id));
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }


    @PostMapping("/selectByid")
    @ApiOperation("根据图片id查询轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "轮播图片id", name = "id",paramType = "query"),
    })
    public AjaxResult selectByid(@RequestBody Image image) {
        return AjaxResult.success(imageService.selectByid(image.getId()));
    }


    @PostMapping("/deleteByid")
    @ApiOperation("根据图片id删除轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "轮播图片id", name = "id",paramType = "query"),
    })
    public AjaxResult deleteByid(@RequestHeader("token") String token,@RequestBody Image image) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        return AjaxResult.success("删除成功",imageService.delete(image.getId()));
    }

}
