package com.zhwl.demo.controller;

import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.config.RuoYiConfig;
import com.zhwl.demo.po.Collect;
import com.zhwl.demo.utils.FileUploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("")
@Api(tags = "图片上传模块")
@Slf4j
@Validated
public class UploadController {

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public AjaxResult add(MultipartFile image) throws IOException {

        String filePath = RuoYiConfig.getUploadPath();
        String imgUrl = "";
        if(image==null){
            return AjaxResult.error("图片不能为空");
        }else {
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, image);
            imgUrl =  fileName;
            imgUrl=imgUrl.substring(8);
        }
        imgUrl= "https://ddm-cu.com"+imgUrl;
        return  AjaxResult.success("上传成功",imgUrl);
    }


}

