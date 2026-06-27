package com.zhwl.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Collect;
import com.zhwl.demo.po.Goods;
import com.zhwl.demo.po.SelectGood;
import com.zhwl.demo.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author txq
 * @Date 2023/6/19 15:17
 * @Version 1.0
 */

@RestController
@RequestMapping("/good")
@Api(tags = "商品模块")
@Slf4j
@Validated
public class GoodController {

    @Autowired
    private GoodService goodService;

    @Autowired
    private GooddetailService gooddetailService;

    @Autowired
    private ShaixuService shaixuService;

    @Autowired
    private cshtml csshtml;

    @Autowired
    private FenLeideService fenLeideService;


    @PostMapping("selectcates")
    @ApiOperation("根据分类类别查询商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品分类id", name = "urlid",paramType = "query"),
            @ApiImplicitParam(value = "页码数（1展示第一页2展示第二页，一页50条数据）", name = "pagecount",paramType = "query"),
            @ApiImplicitParam(value = "价格顺序，0不排序，1高到低，2低到高（按价格排序的时候，时间，热度传值0）", name = "priceType", paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "时间顺序，0不排序，1高到低，2低到高（按时间排序的时候，价格，热度传值0）", name = "timeType",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "热度顺序，0不排序，1高到低，2低到高（按热度排序的时候，时间，价格传值0）", name = "hotType",paramType = "query",dataType = "Int"),
    })
    public AjaxResult selectcates(@RequestBody Goods goods) throws IOException {

        if(goods.getUrlid()==null||goods.getUrlid()==""){
            return AjaxResult.error("商品分类id不能为空");
        }
        return AjaxResult.success(goodService.getGoods(goods.getUrlid(),goods.getPagecount(),goods.getPriceType(),
                goods.getTimeType(),goods.getHotType()));
    }


    @PostMapping("selectFelede")
    @ApiOperation("查询商品细节分类")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品分类cid", name = "cid",paramType = "String"),
    })
    public AjaxResult selectFelede(@RequestBody JSONObject jsonObject) throws IOException {
        String cid = jsonObject.getString("cid");
        return AjaxResult.success(fenLeideService.getfende(cid));
    }


    @PostMapping("selectSellerGoods")
    @ApiOperation("根据卖家code查询商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "卖家code", name = "seller",paramType = "query"),
            @ApiImplicitParam(value = "页码数（1展示第一页2展示第二页，一页50条数据）", name = "pagecount",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "价格顺序，0不排序，1高到低，2低到高（按价格排序的时候，时间，热度传值0）", name = "priceType", paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "时间顺序，0不排序，1高到低，2低到高（按时间排序的时候，价格，热度传值0）", name = "timeType",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "热度顺序，0不排序，1高到低，2低到高（按热度排序的时候，时间，价格传值0）", name = "hotType",paramType = "query",dataType = "Int"),
    })
    public AjaxResult selectSellerGoods(@RequestBody JSONObject jsonObject) throws IOException {
        String seller = jsonObject.getString("seller");
        Integer pagecount = jsonObject.getInteger("pagecount");
        Integer priceType = jsonObject.getInteger("priceType");
        Integer timeType = jsonObject.getInteger("timeType");
        Integer hotType = jsonObject.getInteger("hotType");
        return AjaxResult.success(goodService.getSellerGoods(seller,pagecount,priceType,timeType,hotType));
    }



    @PostMapping("selectGoodDetail")
    @ApiOperation("查询商品详情")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "商品详情跳转连接", name = "breakurl",paramType = "query"),
        @ApiImplicitParam(value = "商品编码", name = "code",paramType = "query"),
        @ApiImplicitParam(value = "出品者", name = "seller",paramType = "query"),
    })
    public AjaxResult selectGoodDetail(@RequestBody Goods goods) throws IOException {

        HashMap<String,Object> maps = gooddetailService.getGoods(goods.getBreakurl(),goods.getCode(),goods.getSeller());

        if(!maps.isEmpty()){
            return AjaxResult.success(maps);
        }else {

            return  AjaxResult.error("未查询到此商品");
        }


    }

    @PostMapping("selectYF")
    @ApiOperation("根据商品code查询运费")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品编码", name = "code",paramType = "query"),
    })
    public AjaxResult selectYF(@RequestBody Goods goods) throws IOException {
        return csshtml.cs(goods.getCode());
    }



    @PostMapping("selectGood")
    @ApiOperation("条件筛选查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品分类id（三级子类别id）", name = "urlid",paramType = "query"),
            @ApiImplicitParam(value = "关键词", name = "keyword",paramType = "query"),
            @ApiImplicitParam(value = "价格范围id(0代表0-5000；1代表5000-1万，依次类推)", name = "priceType",paramType = "query"),
            @ApiImplicitParam(value = "新旧（0不指定1新品2中古）", name = "isNewType",paramType = "query"),
            @ApiImplicitParam(value = "卖家（0所有卖家1正规店铺2个人）", name = "sellType",paramType = "query"),
            @ApiImplicitParam(value = "价格顺序，0不排序，1高到低，2低到高（按价格排序的时候，时间，热度传值0）", name = "priceOderType",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "时间顺序，0不排序，1高到低，2低到高（按时间排序的时候，价格，热度传值0）", name = "timeType",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "热度顺序，0不排序，1高到低，2低到高（按热度排序的时候，时间，价格传值0）", name = "hotType",paramType = "query",dataType = "Int"),
            @ApiImplicitParam(value = "页码数（1展示第一页2展示第二页，一页50条数据）", name = "pagecount",paramType = "query"),
    })
    public AjaxResult selectgood(@RequestBody SelectGood selectGood) throws IOException {
        return AjaxResult.success(shaixuService.getGoods(selectGood.getUrlid(),selectGood.getPagecount(),selectGood.getKeyword(),selectGood.getPriceType(),
                selectGood.getIsNewType(),selectGood.getSellType(),selectGood.getPriceOderType(),selectGood.getTimeType(),selectGood.getHotType()));
    }


}
