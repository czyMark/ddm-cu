package com.zhwl.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.Collect;
import com.zhwl.demo.po.ShppingCart;
import com.zhwl.demo.service.GetMecariDetail;
import com.zhwl.demo.service.MrShppingCartService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @Author txq
 * @Date 2023/7/28 17:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/MrShppingCar")
@Api(tags = "煤炉购物车模块")
@Slf4j
@Validated
public class MrShppingCartController {

    @Autowired
    private MrShppingCartService mrShppingCartService;

    @Autowired
    private GetMecariDetail getMecariDetail;

    @PostMapping("/add")
    @ApiOperation("新增购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品主图url地址", name = "imageurl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品编号", name = "goodcode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "价格", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult add(@RequestBody ShppingCart shppingCart) {
        try {
            JSONObject jsonObject = getMecariDetail.notice(shppingCart.getGoodcode());
            String status = jsonObject.getString("status");
            if(status.equals("sold_out")||status.equals("trading")){
                //删除失效收藏
                return AjaxResult.error("此商品已售");
            }
        }catch (Exception ignored){
            return AjaxResult.error("此商品已售");
        }

        if(shppingCart.getPrice()==null){
            return AjaxResult.error("商品价格不能为空");
        }
        if(mrShppingCartService.selectBycode(shppingCart.getGoodcode(),shppingCart.getUserid())!=null){
            return AjaxResult.error("此商品已加入购物车");
        }

        return AjaxResult.success(mrShppingCartService.add(shppingCart.getImageurl(),shppingCart.getGoodcode(),shppingCart.getTitle(),shppingCart.getPrice(),shppingCart.getUserid()));

    }


    @PostMapping("/selectbyid")
    @ApiOperation("根据用户id查询购物车列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult selectbyid(@RequestBody ShppingCart shppingCart) {
        if(shppingCart.getUserid()==null){
            return AjaxResult.error("用户id不能为空");
        }

        List<ShppingCart> shppingCarts = mrShppingCartService.selectbyid(shppingCart.getUserid());
        List<ShppingCart> newcollects = new ArrayList<>();

            for (int i=0;i<shppingCarts.size();i++){
                try {
                JSONObject jsonObject = getMecariDetail.notice(shppingCarts.get(i).getGoodcode());
                String status = jsonObject.getString("status");
                if(status.equals("sold_out")||status.equals("trading")){
                    //删除失效收藏
                    mrShppingCartService.deleteGoodCode(shppingCarts.get(i).getGoodcode(), shppingCart.getUserid());
                }else {
                    newcollects.add(shppingCarts.get(i));
                }
                }catch (Exception ignored){
                    mrShppingCartService.deleteGoodCode(shppingCarts.get(i).getGoodcode(), shppingCart.getUserid());
                }
            }
        return AjaxResult.success(newcollects);
    }

    @PostMapping("/deleteByid")
    @ApiOperation("根据购物车商品id和用户id删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "购物车商品id", name = "id",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult deleteByid(@RequestBody ShppingCart shppingCart) {
        if(shppingCart.getUserid()==null||shppingCart.getId()==null){
            return AjaxResult.error("用户id不能为空或者购物车商品id不能为空");
        }
        return AjaxResult.success(mrShppingCartService.deleteByid(shppingCart.getId(),shppingCart.getUserid()));
    }

    @PostMapping("/deleteAll")
    @ApiOperation("根据用户id清空购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult deleteAll(@RequestBody ShppingCart shppingCart) {
        if(shppingCart.getUserid()==null){
            return AjaxResult.error("用户id不能为空");
        }
        return AjaxResult.success(mrShppingCartService.deleteAll(shppingCart.getUserid()));
    }


}
