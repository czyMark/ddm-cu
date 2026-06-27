package com.zhwl.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.MercariGoods;
import com.zhwl.demo.service.FenLeideService;
import com.zhwl.demo.service.GetMecariDetail;
import com.zhwl.demo.service.MercariGoodService;
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

/**
 * @Author txq
 * @Date 2023/7/22 0:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/Mercarigood")
@Api(tags = "煤炉商品模块")
@Slf4j
@Validated
public class MercariGoodController {

    @Autowired
    private MercariGoodService mercariGoodService;

    @Autowired
    private GetMecariDetail getMecariDetail;

    @Autowired
    private FenLeideService fenLeideService;

    @PostMapping("selectcates")
    @ApiOperation("根据分类类别查询商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品分类id", name = "urlid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "数据条数", name = "pageSize",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "页数", name = "pageCount",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "关键词", name = "keyWord",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "上架时间 0或者空不排序  1新到旧,2是旧到新（上架顺序和价格顺序只能按一种排序）", name = "isNewType",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "价格顺序 0或者空不排序  1高到低 2 低到高 （上架顺序和价格顺序只能按一种排序）", name = "priceOderType",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "价格区间 低价格", name = "lowPrice",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "价格区间 高价格", name = "maxPrice",paramType = "query",dataType = "string"),

    })
    public AjaxResult selectcates(@RequestBody MercariGoods goods) throws IOException {

        JSONObject jsonObject = mercariGoodService.getGoods(goods.getUrlid(),goods.getPageSize(),goods.getPageCount(),goods.getKeyWord(),goods.getIsNewType(),
                goods.getPriceOderType(),goods.getLowPrice(),goods.getMaxPrice());

        /*JSONArray items = jsonObject.getJSONArray("items");
        for (int i=0;i<items.size();i++){
            JSONObject ss = items.getJSONObject(i);
            JSONArray thumbnails = ss.getJSONArray("thumbnails");
            for (int k=0;k<thumbnails.size();k++){
                String imgurl = thumbnails.get(k).toString();
                imgurl = imgurl.split("\\?")[0];
                log.info(imgurl);
                String base64  =  ImageBase64Util.imageUrlToBase64(imgurl);
                //data:image/jpeg;base64,
                base64 = "data:image/jpeg;base64,"+base64;
                thumbnails.set(k,base64);
                }
            items.set(i,ss);
        }
        jsonObject.put("items",items);*/
        return AjaxResult.success(jsonObject);
    }

    @PostMapping("selectFelede")
    @ApiOperation("查询商品细节分类")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品分类cid", name = "cid",paramType = "String"),
    })
    public AjaxResult selectFelede(@RequestBody JSONObject jsonObject) throws IOException {
        String cid = jsonObject.getString("cid");
        return AjaxResult.success(fenLeideService.getfendeMr(cid));
    }



    @PostMapping("selectdetial")
    @ApiOperation("传入商品id查询商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品编码，商品id", name = "goodcode",paramType = "query"),
    })
    public AjaxResult selectdetial(@RequestBody MercariGoods goods) throws IOException {
        if(goods.getGoodcode()==null||goods.getGoodcode()==""){
            return AjaxResult.error("商品编码有错误");
        }
        return AjaxResult.success(getMecariDetail.notice(goods.getGoodcode()));
    }


}
