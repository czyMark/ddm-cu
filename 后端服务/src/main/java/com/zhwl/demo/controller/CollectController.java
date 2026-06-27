package com.zhwl.demo.controller;

import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Collect;
import com.zhwl.demo.service.CollectService;
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
import java.util.HashMap;

/**
 * @Author txq
 * @Date 2023/7/12 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/collect")
@Api(tags = "收藏模块")
@Slf4j
@Validated
public class CollectController {

    @Autowired
    private CollectService collectService;


    @PostMapping("/add")
    @ApiOperation("新增收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "0雅虎，1煤炉", name = "platform",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品编码", name = "goodcode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品主图url地址", name = "imageUrl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "出价次数", name = "counts",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "现价", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult add(@RequestBody Collect collect) {
        return AjaxResult.success(collectService.add(collect.getPlatform(),collect.getGoodcode(),collect.getImageUrl(),
                collect.getTitle(),collect.getCounts(),collect.getPrice(),collect.getUserid()));
    }

    @PostMapping("/deleteByCode")
    @ApiOperation("根据商品编码删除收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品编码", name = "goodcode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult deleteByCode(@RequestBody Collect collect) {
        return AjaxResult.success(collectService.deleteByCode(collect.getGoodcode(),collect.getUserid()));
    }



    @PostMapping("/selectbyid")
    @ApiOperation("根据用户id和平台查询个人收藏列表(type=0商品失效  1 正常更新)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
            @ApiImplicitParam(value = "0雅虎，1煤炉", name = "platform",paramType = "query"),
    })
    public AjaxResult selectbyid(@RequestBody Collect collect) throws IOException {
        return AjaxResult.success(collectService.selectbyid(collect.getUserid(),collect.getPlatform()));
    }


    @PostMapping("/selectGoodCollect")
    @ApiOperation("根据用户id和商品编码查询商品是否收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
            @ApiImplicitParam(value = "商品编号", name = "goodcode",paramType = "query"),
    })
    public AjaxResult selectb(@RequestBody Collect collect) {

        Integer type = 0;

        if(collect.getUserid()==null){
            return AjaxResult.error("用户id不能为空");
        }
        if(collect.getGoodcode()==null){
            return AjaxResult.error("商品编码不能为空");
        }

        if(collectService.selectGoodCollect(collect.getUserid(),collect.getGoodcode())!=null){
            type = 1;
        }
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("type",type);

        return AjaxResult.success(hashMap);
    }


}
