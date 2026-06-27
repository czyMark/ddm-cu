package com.zhwl.demo.controller;


import cn.hutool.core.net.URLDecoder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.*;
import com.zhwl.demo.service.*;
import com.zhwl.demo.utils.OrderNoUtil;
import com.zhwl.demo.utils.RedisUtils;

import com.zhwl.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author txq
 * @Date 2023/7/16 18:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/yahooOrder")
@Api(tags = "雅虎订单模块")
@Slf4j
@Validated
public class GoodOrderController {

    @Autowired
    private GoodOrderService goodOrderService;

    @Autowired
    private UserService userService;


    @Autowired
    private cshtml csshtml;

    @Autowired
    private Mrcshtml mrcshtml;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private PayCjPrice payCjPrice;

    @Autowired
    private CreatebidpreviewService createbidpreviewService;

    @Autowired
    private JijueService jijueService;

    @Autowired
    private YuyueService yuyueService;

    @Autowired
    private MerTbService merTbService;

    @Autowired
    private CsPsot csPsot;

    @PostMapping("pay")
    @ApiOperation("生成立即出价订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "出价金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "商品标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品主图url地址", name = "imageurl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品编号", name = "goodCode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "出价类型（0立即出价,1即绝出价,2预约出价）", name = "type",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "是否含税（0不含税,1含税）", name = "ratetype",paramType = "query",dataType = "int"),
    })
    public AjaxResult addNotice(@RequestBody JSONObject jsonObject) throws IOException {
        BigDecimal price = jsonObject.getBigDecimal("price");
        Integer userid = jsonObject.getInteger("userid");
        String goodCode = jsonObject.getString("goodCode");
        String imageurl = jsonObject.getString("imageurl");
        String title = jsonObject.getString("title");
        Integer type = jsonObject.getInteger("type");
        Integer ratetype = jsonObject.getInteger("ratetype");
        String orderid = OrderNoUtil.getOrderNo();

        //查询用户   押金  符合哪个范围
        User user = userService.selectByid(userid);
        BigDecimal yjBalance = user.getYjBalance();
        //查询用户所有订单的金额
        BigDecimal yjxfpeice = goodOrderService.selectxf(userid);
        if(yjxfpeice==null){
            yjxfpeice = new BigDecimal(0);
        }

        yjxfpeice = yjxfpeice.add(price);

        //押金范围    得出    可下单范围    不满足  返回error
        if( yjBalance.compareTo(new BigDecimal(1000))>=0){
                if (yjBalance.compareTo(new BigDecimal(2000))>=0){
                    if (yjBalance.compareTo(new BigDecimal(5000))>=0){
                        if (yjBalance.compareTo(new BigDecimal(10000))>=0){
                            if (yjBalance.compareTo(new BigDecimal(15000))>=0){
                                if (yjBalance.compareTo(new BigDecimal(30000))>=0){
                                    if (yjBalance.compareTo(new BigDecimal(60000))>=0){
                                        if (yjBalance.compareTo(new BigDecimal(150000))>=0){
                                            if (yjBalance.compareTo(new BigDecimal(300000))>=0){

                                                //这边是押金  300000+
                                                if(yjxfpeice.compareTo(new BigDecimal(1000000))>0){
                                                    return AjaxResult.error("押金不足");
                                                }

                                            }else {
                                                //这边是押金  150000-300000
                                                if(yjxfpeice.compareTo(new BigDecimal(500000))>0){
                                                    return AjaxResult.error("押金不足");
                                                }
                                            }
                                        }else {
                                            //这边是押金  60000-150000
                                            if(yjxfpeice.compareTo(new BigDecimal(200000))>0){
                                                return AjaxResult.error("押金不足");
                                            }
                                        }
                                    }else {
                                        //这边是押金  30000-60000
                                        if(yjxfpeice.compareTo(new BigDecimal(100000))>0){
                                            return AjaxResult.error("押金不足");
                                        }
                                    }
                                }else {
                                    //这边是押金  15000 - 30000
                                    if(yjxfpeice.compareTo(new BigDecimal(50000))>0){
                                        return AjaxResult.error("押金不足");
                                    }
                                }
                            }else {
                                //这边是押金  10000 - 15000
                                if(yjxfpeice.compareTo(new BigDecimal(30000))>0){
                                    return AjaxResult.error("押金不足");
                                }
                            }
                        }else {
                            //这边是押金  5000 - 10000
                            if(yjxfpeice.compareTo(new BigDecimal(10000))>0){
                                return AjaxResult.error("押金不足");
                            }
                        }

                        }else {
                        //这边是押金  2000 - 5000
                        if(yjxfpeice.compareTo(new BigDecimal(5000))>0){
                            return AjaxResult.error("押金不足");
                        }
                    }

                }else {
                    //这边是押金  1000 - 2000
                    if(yjxfpeice.compareTo(new BigDecimal(1000))>0){
                        return AjaxResult.error("押金不足");
                    }

                }
        }else {
            return AjaxResult.error("押金不足");
        }



        //查询用户已经下单数量  超过20单  不让再下单
        /*Integer count = goodOrderService.orderTotal(userid);
        User user = userService.selectByid(userid);
        BigDecimal bigDecimal2 = user.getYjBalance();
        BigDecimal bigDecimal1 = new BigDecimal(2000);
        bigDecimal2 = bigDecimal2.divide(bigDecimal1, 2, BigDecimal.ROUND_HALF_UP);
        Integer ww  = (int) bigDecimal2.setScale( 0, RoundingMode.DOWN).longValue(); // 向下取整
        ww = ww*20;
        if(count>ww){
            return  AjaxResult.error("下单数量超出押金金额");
        }*/

        if(type==1){
            //立即生成    即绝订单
            return jijueService.getGoods(goodCode,price,title,imageurl,userid,orderid,ratetype);
        }else {
            if(type==0){
                //去生成订单   正常后出价订单
                return createbidpreviewService.getGoods(goodCode,price,title,imageurl,userid,orderid,ratetype);
            }else {
                //创建预约订单
                //return AjaxResult.success();
                return goodOrderService.addyy(goodCode,title,imageurl,userid,price,orderid,ratetype);
            }
        }

    }


/*    @PostMapping("cs")
    @ApiOperation("测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品详情跳转连接", name = "breakurl",paramType = "query"),
            @ApiImplicitParam(value = "商品编码", name = "code",paramType = "query"),
            @ApiImplicitParam(value = "出品者", name = "seller",paramType = "query"),
    })
    public AjaxResult cs(@RequestBody Goods goods) throws IOException {
        return AjaxResult.success(csPsot.post("https://api.mercari.jp/transactions/buy","buyer_id=541829980&consume_funds=0&consume_point=0&consume_docomo_point=0&consume_sales=0&payment_use_last_card=true&payment_amount=300&payment_fee=0&pay_times=1&pay_method=1&seller_id=167020036&item_id=m44623955140&item_name=d%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%A0+d+program%E7%AD%89%E3%80%80%E8%A9%A6%E4%BE%9B%E5%93%81%E3%82%BB%E3%83%83%E3%83%88&price=300&description=%E7%A8%AE%E9%A1%9E%C2%B7%C2%B7%C2%B7%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%2F%E8%A9%A6%E4%BE%9B%E5%93%81%0A%0A%EF%BD%84%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%A0%0A%E3%83%A2%E3%82%A4%E3%82%B9%E3%83%88%E3%82%B1%E3%82%A2%E3%80%80%E3%82%A8%E3%83%9E%E3%83%AB%E3%82%B8%E3%83%A7%E3%83%B3%E3%80%80MB+%C3%97+1%0A%0A%E3%83%A2%E3%82%A4%E3%82%B9%E3%83%88%E3%82%B1%E3%82%A2%E3%80%80%E3%83%AD%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E3%80%80MB+%C3%97+1%0A%0A%E3%82%AB%E3%83%B3%E3%83%80%E3%83%B3%E3%83%90%E3%83%AA%E3%82%A2%E3%80%80%E3%82%A8%E3%83%83%E3%82%BB%E3%83%B3%E3%82%B9%E3%80%80%C3%97+3%0A%E3%80%80%0A%E3%81%8A%E3%81%BE%E3%81%91%E3%81%A7RMK%E3%83%AA%E3%82%AF%E3%82%A4%E3%83%89%E3%83%95%E3%82%A1%E3%83%B3%E3%83%87%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%C3%972%E3%81%8A%E4%BB%98%E3%81%91%E3%81%97%E3%81%BE%E3%81%99%E3%80%82%0A%0A%E3%81%A9%E3%81%86%E3%81%9E%E3%82%88%E3%82%8D%E3%81%97%E3%81%8F%E3%81%8A%E9%A1%98%E3%81%84%E3%81%84%E3%81%9F%E3%81%97%E3%81%BE%E3%81%99%E3%80%82%0A%0A%0A%E7%A8%AE%E9%A1%9E%C2%B7%C2%B7%C2%B7%E7%BE%8E%E5%AE%B9%E6%B6%B2%2F%E3%82%BB%E3%83%A9%E3%83%A0&brand_name=3139&category_id=1266&item_condition=1&shipping_payer=2&shipping_method=5&shipping_duration=2&shipping_from_area=99&shipping_class=0&_checksum=2893e146e51946677daf0b3a38716b4f&id=73696261&zip_code1=557&zip_code2=0061&family_name=%E5%AE%87%E5%A5%87&first_name=%E5%9B%BD%E9%9A%9B&family_name_kana=%E3%82%A6%E3%82%AD&first_name_kana=%E3%82%B3%E3%82%AF%E3%82%B5%E3%82%A4&prefecture=%E5%A4%A7%E9%98%AA%E5%BA%9C&city=%E5%A4%A7%E9%98%AA%E5%B8%82%E8%A5%BF%E6%88%90%E5%8C%BA&address1=%E5%8C%97%E6%B4%A5%E5%AE%884-4-75-47&address2=&telephone=08068048053&state_abbreviation=%E5%A4%A7%E9%98%AA%E5%BA%9C&purchase_session_id=57c04cb0557623860d1be4f8e9f53bdc&ship_to_address_id=73696261&paid_method=deferred_payment"));
    }*/



    @PostMapping("payCjPrice")
    @ApiOperation("竞拍成功（补差价）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "差价金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "主订单自增id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult payCjPrice(@RequestBody JSONObject jsonObject) {
        BigDecimal price = jsonObject.getBigDecimal("price");
        Integer userid = jsonObject.getInteger("userid");
        Integer id = jsonObject.getInteger("id");
        //return  AjaxResult.success(PayUtil.createOder("竞拍成功补差价",price,user.getOpenid(),attachType));
        return payCjPrice.payCjPrice(price,userid,id);
    }


    @PostMapping("payJpPrice")
    @ApiOperation("竞拍成功（补差价）微信支付")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "差价金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "主订单自增id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult payJpPrice(@RequestBody JSONObject jsonObject) throws Exception {
        BigDecimal price = jsonObject.getBigDecimal("price");
        Integer userid = jsonObject.getInteger("userid");
        Integer id = jsonObject.getInteger("id");
        User user = userService.selectByid(userid);
        String attachType = "5:" + id +":"+userid;

        List<String> list  = new ArrayList<>();


        return  AjaxResult.success(PayUtil.createOder("竞拍成功补差价",price,user.getOpenid(),userid,"X",list));
    }






    @PostMapping("/SelectOrderByType")
    @ApiOperation("根据用户id和状态查询订单 0全部订单,1已付款（出价中）,2竞拍成功（补差价） 3竞拍失败,4待入库，5已入库 6申请发货（合并订单中）,7待付尾款，8、代发货,9已发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult SelectOrderByType(@RequestBody JSONObject jsonObject) {
        return AjaxResult.success(goodOrderService.selectMyorder(jsonObject.getByte("type"),jsonObject.getInteger("userid")));

    }

    @PostMapping("/updataTypeByid")
    @ApiOperation("根据用户id和type状态修改订单状态 后台修改待入库、入库时候使用  其他状态单独接口  1已付款（出价中）,2竞拍成功（补差价） 3竞拍失败,4待入库，5已入库 6申请发货（合并订单中）,7待付尾款，8、代发货,9已发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "订单自增主id", name = "id",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataTypeByid(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        Integer userid = jsonObject.getInteger("userid");
        Integer id = jsonObject.getInteger("id");
        Byte type = jsonObject.getByte("type");
        return AjaxResult.success(goodOrderService.updataTypeByid(type,id,userid));

    }




    @PostMapping("/updataTypeByOrderid")
    @ApiOperation("根据用户Orderid和type状态修改订单状态 后台修改待入库、入库时候使用  其他状态单独接口  1已付款（出价中）,2竞拍成功（补差价） 3竞拍失败,4待入库，5已入库 6申请发货（合并订单中）,7待付尾款，8、代发货,9已发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "订单自增主id", name = "orderid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataTypeByorderid(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        Integer userid = jsonObject.getInteger("userid");
        String orderid = jsonObject.getString("orderid");
        Byte type = jsonObject.getByte("type");
        return AjaxResult.success(goodOrderService.updataTypeByorderid(type,orderid,userid));

    }


    @PostMapping("/updataPhoneTypeByOrderid")
    @ApiOperation("前台根据用户Orderid和type状态修改订单状态 后台修改待入库、入库时候使用  其他状态单独接口  1已付款（出价中）,2竞拍成功（补差价） 3竞拍失败,4待入库，5已入库 6申请发货（合并订单中）,7待付尾款，8、代发货,9已发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "订单号", name = "orderid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "首次支付金额(首次付款传  其他不传)", name = "firstPayPrice",paramType = "query",dataType = "String")
    })
    public AjaxResult updataPhoneTypeByOrderid(@RequestBody JSONObject jsonObject) {

        Integer userid = jsonObject.getInteger("userid");
        String orderid = jsonObject.getString("orderid");
        Byte type = jsonObject.getByte("type");
        Byte xdtype = 4;
        /*Byte TBtype = 1;
        if(type==TBtype){
            GoodOrder merOrder = goodOrderService.selectmainorder(orderid,userid);
            merTbService.xd(merOrder.getGoodCode());
        }*/
        if(type==xdtype){
            //查询用户id   修改自身消费金额   add
            User user = userService.selectByid(userid);
            BigDecimal myxfprice = user.getMyxfprice();
            GoodOrder merOrder = goodOrderService.selectmainorder(orderid,userid);
            myxfprice = myxfprice.add(merOrder.getPrice());
            //修改用户的自己消费金额
            userService.xfpeice(myxfprice,userid);

            if( user.getInvitecode()!=""&&user.getInvitecode()!=null){
                //给上级  增加消费金额
                User userup = userService.Selectmyinvitecode(user.getInvitecode());
                //给上级增加   下级消费总金额
                BigDecimal xjxfprice = userup.getXjxfprice().add(merOrder.getPrice());
                userService.xjxfpeice(xjxfprice,userid);
            }
            BigDecimal firstPayPrice = jsonObject.getBigDecimal("firstPayPrice");
            return AjaxResult.success(goodOrderService.updataTypeByorderids(firstPayPrice,type,orderid,userid));
        }else {
            return AjaxResult.success(goodOrderService.updataTypeByorderid(type,orderid,userid));

        }
    }




    @PostMapping("/createmergeOrder")
    @ApiOperation("合并订单 用户申请发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单id数组", name = "order",paramType = "query",dataType = "JSONArray"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "地址id", name = "addressid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "路线信息", name = "luxian",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "路线备注", name = "lxRemarks",paramType = "query",dataType = "string"),
    })
    public AjaxResult createmergeOrder(@RequestBody JSONObject jsonObject) {

        return AjaxResult.success(goodOrderService.createmergeOrder(jsonObject));

    }




    @PostMapping("/hborder")
    @ApiOperation("煤炉雅虎合并订单 用户申请发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "雅虎订单id数组", name = "order",paramType = "query",dataType = "JSONArray"),
            @ApiImplicitParam(value = "煤炉订单id数组", name = "ordermr",paramType = "query",dataType = "JSONArray"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "地址id", name = "addressid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "路线信息", name = "luxian",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "路线备注", name = "lxRemarks",paramType = "query",dataType = "string"),
    })
    public AjaxResult hborder(@RequestBody JSONObject jsonObject) {
        return AjaxResult.success(goodOrderService.createmergeOrderMr(jsonObject));

    }



    @PostMapping("/updataJpyunfeiType")
    @ApiOperation("根据定orderid修改是否包邮  0不包邮，1包邮")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单号orderid", name = "orderid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = " 0不包邮，1包邮", name = "jpyunfeiType",paramType = "query",dataType = "string"),
    })
    public AjaxResult updataJpyunfeiType(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        String orderid = jsonObject.getString("orderid");
        Integer jpyunfeiType = jsonObject.getInteger("jpyunfeiType");

        return AjaxResult.success(goodOrderService.updataJpyunfeiType(orderid,jpyunfeiType));

    }


    @PostMapping("/updataFy")
    @ApiOperation("管理员计算费用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代购费用", name = "dgPrice",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "日本运费", name = "jpyunfei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "国际运费", name = "gjyunfei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "仓储费用", name = "cangcufei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "包装费用", name = "baozhuangfei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "其他费用", name = "otherPrice",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "手续费", name = "shouxufei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "其他费用备注", name = "otherPriceContent",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单自增主id", name = "id",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "string"),
    })
    public AjaxResult updataFy(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }


        BigDecimal dgPrice = jsonObject.getBigDecimal("dgPrice");
        BigDecimal jpyunfei= jsonObject.getBigDecimal("jpyunfei");
        BigDecimal gjyunfei= jsonObject.getBigDecimal("gjyunfei");
        BigDecimal cangcufei= jsonObject.getBigDecimal("cangcufei");
        BigDecimal baozhuangfei= jsonObject.getBigDecimal("baozhuangfei");
        BigDecimal otherPrice= jsonObject.getBigDecimal("otherPrice");
        String otherPriceContent = jsonObject.getString("otherPriceContent");
        BigDecimal shouxufei = new BigDecimal(0);
        if(jsonObject.containsKey("shouxufei")){
            shouxufei = jsonObject.getBigDecimal("shouxufei");
        }
        Integer id = jsonObject.getInteger("id");
        Integer userid = jsonObject.getInteger("userid");
        if(goodOrderService.updataFy(dgPrice,jpyunfei,gjyunfei,cangcufei,baozhuangfei,otherPrice,otherPriceContent,shouxufei,id,userid)==0){
            return AjaxResult.error("更新失败");
        }
        return AjaxResult.success("更新成功");

    }

    @PostMapping("/payFinalPrice")
    @ApiOperation("用户付尾款")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "尾款总金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "主订单自增id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult payFinalPrice(@RequestBody JSONObject jsonObject) throws Exception {

        Integer userid = jsonObject.getInteger("userid");
        User user = userService.selectByid(userid);
        Integer id = jsonObject.getInteger("id");
        BigDecimal price = jsonObject.getBigDecimal("price");
        //计算下总尾款
        GoodOrder goodOrder = goodOrderService.selectByid(id);

        BigDecimal totalPrice = goodOrder.getDgPrice().add(goodOrder.getJpyunfei().add(goodOrder.getGjyunfei().add(goodOrder.getCangcufei().add(goodOrder.getBaozhuangfei().add(goodOrder.getOtherPrice())))));

        totalPrice = totalPrice.subtract(price);

        /*if(totalPrice.compareTo(BigDecimal.ZERO)!=0){
            return AjaxResult.error("尾款金额不一致");
        }*/
        //拼接传参
        List<String> list = new ArrayList<>();
        return  AjaxResult.success(PayUtil.createOderRen("用户充值",price,user.getOpenid(),userid,"X",list));


    }

    @PostMapping("/sendGood")
    @ApiOperation("管理员发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "物流公司", name = "wlcompany",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "物理单号", name = "trackingNumber",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "备注", name = "wlRemarks",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单号orderid", name = "orderid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult sendGood(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) throws Exception {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        String wlcompany = jsonObject.getString("wlcompany");
        String trackingNumber = jsonObject.getString("trackingNumber");
        String wlRemarks = jsonObject.getString("wlRemarks");
        String orderid = jsonObject.getString("orderid");
        Integer userid = jsonObject.getInteger("userid");
        //修改订单状态   根据主id 先修改主订单   物流信息
        goodOrderService.updataWlnew(wlcompany,trackingNumber,wlRemarks,orderid,userid);
        Byte type = 9;
        goodOrderService.updataTypeByidnew(type,orderid,userid);
        //查询主订单id
        GoodOrder goodOrder = goodOrderService.selectmainorder(orderid,userid);

        // 遍历子订单   修改子订单   物流信息
        List<GoodOrder> goodOrderList = goodOrderService.selectByParentid(goodOrder.getId());
        for (int i=0;i<goodOrderList.size();i++){
            goodOrderService.updataWl(wlcompany,trackingNumber,wlRemarks,goodOrderList.get(i).getId(),userid);
            goodOrderService.updataTypeByid(type,goodOrderList.get(i).getId(),goodOrderList.get(i).getUserid());
        }
        return  AjaxResult.success("物流信息更新成功");
    }


    @PostMapping("/SelectAll")
    @ApiOperation("分页查询所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码，0表示第一页", name = "pageNumber",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "每页结果数", name = "pageSize",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "用户名", name = "nickName",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单编号", name = "orderid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单类型 0全部订单,1已付款（出价中）,2竞拍成功（补差价） 3竞拍失败,4待入库，5已入库 6申请发货（合并订单中）,7待付尾款，8、代发货,9已发货", name = "type",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品名称模糊查询", name = "title",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "（订单创建时间）" + "", name = "begintime",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "（订单创建时间）结束时间", name = "endtime",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "商品code", name = "goodCode",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "运费状态，不传查所有 0不包邮  1包邮,2包邮不包邮合集（非空）", name = "jpyunfeiType",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "首付款 空查全部 1有值 0没值", name = "isHaveFirstPayPrice",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "下单账号id", name = "accountid",paramType = "query",dataType = "int"),

    })
    public AjaxResult SelectAll(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        String goodCode = jsonObject.getString("goodCode");
        Integer userid = jsonObject.getInteger("userid");

        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");
        String nickName = jsonObject.getString("nickName");
        String orderid = jsonObject.getString("orderid");
        Integer type = jsonObject.getInteger("type");
        String title = jsonObject.getString("title");
        String begintime = jsonObject.getString("begintime");
        String endtime = jsonObject.getString("endtime");
        Integer jpyunfeiType = jsonObject.getInteger("jpyunfeiType");
        String isHaveFirstPayPrice = jsonObject.getString("isHaveFirstPayPrice");
        Integer accountid = 0;
        if (jsonObject.containsKey("accountid")){
            accountid = jsonObject.getInteger("accountid");
        }



        return AjaxResult.success(goodOrderService.selectAll(pageNumber,pageSize,jpyunfeiType,nickName,orderid,type,title,begintime,endtime,userid,goodCode,isHaveFirstPayPrice,accountid));

    }

    @PostMapping("/SelectByid")
    @ApiOperation("根据订单自增主id查询订单详情 物流信息   地址   用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "订单自增主id", name = "id",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "地址id", name = "addressid",paramType = "query",dataType = "int"),
    })
    public AjaxResult SelectByid(@RequestBody JSONObject jsonObject) {


        Integer userid = jsonObject.getInteger("userid");
        Integer id = jsonObject.getInteger("id");
        Integer addressid = jsonObject.getInteger("addressid");
        return AjaxResult.success(goodOrderService.selectByOrderid(userid,id,addressid));

    }


    @PostMapping("/deleteByid")
    @ApiOperation("根据订单自增主id删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单自增主id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult deleteByid(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        Integer id = jsonObject.getInteger("id");
        return goodOrderService.deleteByid(id);

    }


    @CrossOrigin
    @PostMapping("excel")
    @ApiOperation("导出待入库订单")
    @ApiImplicitParams({
    })
    public void export(HttpServletResponse response,@RequestBody JSONObject jsonData) throws IOException {
        //最终转出json
        List<RuKu> list = new ArrayList<>();
        JSONArray jsonArray  = jsonData.getJSONArray("list");
        list = JSONObject.parseArray(jsonArray.toString(),RuKu.class);
        log.info(list.get(0).getTitle());
        ExcelUtil<RuKu> util = new ExcelUtil<>(RuKu.class);
        util.exportExcel(response, list, "待入库订单");

    }


    @CrossOrigin
    @PostMapping("excelout")
    @ApiOperation("导出待出库订单")
    @ApiImplicitParams({
    })
    public void excelout(HttpServletResponse response,@RequestBody JSONObject jsonData) throws IOException {
        //最终转出json
        List<ChuKu> list = new ArrayList<>();
        JSONArray jsonArray  = jsonData.getJSONArray("list");
        list = JSONObject.parseArray(jsonArray.toString(),ChuKu.class);
        ExcelUtil<ChuKu> util = new ExcelUtil<>(ChuKu.class);
        util.exportExcel(response, list, "待出库订单");

    }



    /*@PostMapping("/exportOrder")
    @ApiOperation("导出雅虎订单")
    public AjaxResult exportUser(HttpServletResponse response,@RequestBody JSONObject jsonData) throws Exception {


        //最终转出json
        JSONArray exportJson  = new JSONArray();

        List<OrderExport> list = new ArrayList<>();
        JSONObject jsonObject = jsonData;
        JSONArray jsonArray  = jsonObject.getJSONArray("list");
        //先给单个订单付值   循环便利    主订单导出
        for(int i=0;i<jsonArray.size();i++){
            //主订单
            OrderExport orderExportParent = new OrderExport();
            JSONObject orderParentjs = jsonArray.getJSONObject(i);
            orderExportParent.setOrderType("主订单");
            orderExportParent.setOrderid(orderParentjs.getString("orderid"));
            orderExportParent.setGoodCode(orderParentjs.getString("goodCode"));
            orderExportParent.setPrice(orderParentjs.getBigDecimal("price"));
            orderExportParent.setType(orderParentjs.getInteger("type"));
            orderExportParent.setUserid(orderParentjs.getInteger("userid"));
            //用户信息
            JSONObject jsonuser = orderParentjs.getJSONObject("user");
            orderExportParent.setNickName(jsonuser.getString("nickName"));

            //地址信息
            JSONObject jsonAddress = orderParentjs.getJSONObject("userAddress");

            String pictureUrl = jsonAddress.getString("idZ");

            byte[] btImg = null;
            byte[] btImg1 = null;
            if(pictureUrl!=null){
                //建立图片连接
                URL url = new URL(pictureUrl);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //设置请求方式
                connection.setRequestMethod("GET");
                //设置超时时间
                connection.setConnectTimeout(10*1000);

                //输入流
                InputStream stream = connection.getInputStream();
                btImg = readInputStream(stream);//得到图片的二进制数据
                //记得关闭流，不然消耗资源
                orderExportParent.setIdZ(btImg);
                stream.close();

                String pictureUrl1 = jsonAddress.getString("idF");
                //建立图片连接
                URL url1= new URL(pictureUrl1);
                HttpURLConnection connection1 = (HttpURLConnection)url1.openConnection();
                //设置请求方式
                connection1.setRequestMethod("GET");
                //设置超时时间
                connection1.setConnectTimeout(10*1000);

                //输入流
                InputStream stream1 = connection1.getInputStream();
                btImg1 = readInputStream(stream1);//得到图片的二进制数据
                //记得关闭流，不然消耗资源
                stream1.close();
                orderExportParent.setIdF(btImg1);
            }

            orderExportParent.setWlcompany(orderParentjs.getString("wlcompany"));
            orderExportParent.setTrackingNumber(orderParentjs.getString("trackingNumber"));
            orderExportParent.setWlRemarks(orderParentjs.getString("wlRemarks"));

            orderExportParent.setAddress(jsonAddress.getString("address"));
            orderExportParent.setUsername(jsonAddress.getString("username"));
            orderExportParent.setPhone(jsonAddress.getString("phone"));

            orderExportParent.setCreatetime(orderParentjs.getDate("createtime"));
            orderExportParent.setDgPrice(orderParentjs.getBigDecimal("dgPrice"));
            orderExportParent.setJpyunfei(orderParentjs.getBigDecimal("jpyunfei"));
            orderExportParent.setGjyunfei(orderParentjs.getBigDecimal("gjyunfei"));
            orderExportParent.setCangcufei(orderParentjs.getBigDecimal("cangcufei"));
            orderExportParent.setBaozhuangfei(orderParentjs.getBigDecimal("baozhuangfei"));
            orderExportParent.setOtherPrice(orderParentjs.getBigDecimal("otherPrice"));
            orderExportParent.setOtherPriceContent(orderParentjs.getString("otherPriceContent"));
            exportJson.add(orderExportParent);
            //便利所有子订单
            JSONArray jsonArraychildren  = orderParentjs.getJSONArray("children");

            for (int k=0;k<jsonArraychildren.size();k++){
                JSONObject orderChildjs = jsonArraychildren.getJSONObject(k);
                OrderExport orderExportchildren = new OrderExport();

                orderExportchildren.setOrderType("子订单");
                orderExportchildren.setOrderid(orderChildjs.getString("orderid"));
                //log.info(orderChildjs.getString("orderid"));
                orderExportchildren.setGoodCode(orderChildjs.getString("goodCode"));
                orderExportchildren.setPrice(orderChildjs.getBigDecimal("price"));
                orderExportchildren.setType(orderChildjs.getInteger("type"));
                orderExportchildren.setUserid(orderChildjs.getInteger("userid"));
                //用户信息
                orderExportchildren.setNickName(jsonuser.getString("nickName"));

                //地址信息
                if(btImg!=null&&btImg1!=null){
                    orderExportchildren.setIdZ(btImg);
                    orderExportchildren.setIdF(btImg1);
                }

                orderExportchildren.setWlcompany(orderChildjs.getString("wlcompany"));
                orderExportchildren.setTrackingNumber(orderChildjs.getString("trackingNumber"));
                orderExportchildren.setWlRemarks(orderChildjs.getString("wlRemarks"));

                orderExportchildren.setAddress(jsonAddress.getString("address"));
                orderExportchildren.setUsername(jsonAddress.getString("username"));
                orderExportchildren.setPhone(jsonAddress.getString("phone"));

                orderExportchildren.setCreatetime(orderChildjs.getDate("createtime"));
                orderExportchildren.setDgPrice(orderChildjs.getBigDecimal("dgPrice"));
                orderExportchildren.setJpyunfei(orderChildjs.getBigDecimal("jpyunfei"));
                orderExportchildren.setGjyunfei(orderChildjs.getBigDecimal("gjyunfei"));
                orderExportchildren.setCangcufei(orderChildjs.getBigDecimal("cangcufei"));
                orderExportchildren.setBaozhuangfei(orderChildjs.getBigDecimal("baozhuangfei"));
                orderExportchildren.setOtherPrice(orderChildjs.getBigDecimal("otherPrice"));
                orderExportchildren.setOtherPriceContent(orderChildjs.getString("otherPriceContent"));
                exportJson.add(orderExportchildren);
            }

        }
        list = JSONObject.parseArray(exportJson.toString(),OrderExport.class);


        excelService.exportExhibitors(response,list);
        //ExcelUtil<OrderExport> util = new ExcelUtil<OrderExport>(OrderExport.class);
        return null;
    }*/


    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }




}
