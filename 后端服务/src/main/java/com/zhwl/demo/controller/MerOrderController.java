package com.zhwl.demo.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.*;
import com.zhwl.demo.service.*;
import com.zhwl.demo.utils.OrderNoUtil;
import com.zhwl.demo.utils.RedisUtils;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author txq
 * @Date 2023/7/29 5:30
 * @Version 1.0
 */
@RestController
@RequestMapping("/MerOrder")
@Api(tags = "煤炉购订单模块")
@Slf4j
@Validated
public class MerOrderController {

    @Autowired
    private MerOrderService merOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private MrExcelService excelService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GetMecariPayDetail getMecariPayDetail;

    @Autowired
    private GetMecariDetail getMecariDetail;

    @Autowired
    private CsPsot csPsot;

    @Autowired
    private MrdpopService mrdpopService;

    @Autowired
    private MerTbService merTbService;




   /* @PostMapping("/payreback")
    @ApiOperation("煤炉单商品支付成功回调")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品主图url地址", name = "imageurl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品编号", name = "goodCode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "价格", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品数量", name = "count",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "地址ID", name = "addressid",paramType = "query",dataType = "int"),
    })
    public AjaxResult pay(@RequestBody MerOrder merOrder) throws Exception {

        if(orderidList[0].equals("1")){
            //编列订单ID    更改订单状态
            for (int i=1;i<orderidList.length;i++){
                merOrderService.updata(orderidList[i],transaction_id);


                //根据订单编号查询 商品编码 和用户ID
                MerOrder shppingCart = merOrderService.selectByorderid(orderidList[i]);
                log.info(shppingCart.getGoodCode());
                //同步订单操作
                merTbService.xd(shppingCart.getGoodCode());
                try{
                    //根基商品编码  和   用户ID删除已购买购物车
                    mrShppingCartService.deleteGoodCode(shppingCart.getGoodCode(),shppingCart.getUserid());

                    List<User> users = userService.selectIsadmin();
                    for (int m=0;m<users.size();m++){
                        //发送通知
                        Template template=new Template();
                        //这里填写模板ID
                        template.setTemplate_id("SIAnL8-hwuUoxe92om2SLXdomZaqo1PacP36QrwXEI0");
                        //这里填写用户的openid
                        template.setTouser(users.get(m).getOpenid());
                        //这里填写点击订阅消息后跳转小程序的界面
                        template.setPage("pages/index/index");
                        List<TemplateParam> paras=new ArrayList<>();
                        paras.add(new TemplateParam("character_string1",orderidList[i]));
                        paras.add(new TemplateParam("time2",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                        paras.add(new TemplateParam("amount3",shppingCart.getPrice().toString()));
                        paras.add(new TemplateParam("thing4","煤炉订单"));
                        template.setTemplateParamList(paras);
                        //发送下单通知
                        sendMessageService.sendTemplateMsg(template);
                    }
                }catch (Exception e){

                }
            }

        }

        return AjaxResult.success("成功");

    }*/


    @PostMapping("/pay")
    @ApiOperation("煤炉单商品直接下单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品主图url地址", name = "imageurl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品编号", name = "goodCode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "价格", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品数量", name = "count",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "地址ID", name = "addressid",paramType = "query",dataType = "int"),
    })
    public AjaxResult pay(@RequestBody MerOrder merOrder) throws Exception {

        //根据用户ID查询openid
        User user = userService.selectByid(merOrder.getUserid());
        String nickName = user.getNickName();
        String orderid = OrderNoUtil.getOrderNo();
        String attachType = orderid;
        BigDecimal counts = new BigDecimal(merOrder.getCount());

        BigDecimal total = merOrder.getFirstPayPrice().multiply(counts);

        //先创建订单
        if(merOrder.getGoodCode()==null){
            return AjaxResult.error("商品编码不能为空");
        }

        //查询库里面有没有  这个上商品编码
        List<MerOrder> merOrder1 = merOrderService.selectBycode(merOrder.getGoodCode());
        log.info(String.valueOf(merOrder1.size()));
        if(merOrder1.size()!=0){
            return AjaxResult.error("此商品已售");
        }

        /*try {
            JSONObject jsonObject = getMecariDetail.notice(merOrder.getGoodCode());

            if(jsonObject.toString().contains("auction_info")){
                return AjaxResult.error("此商品不支持下单");
            }

            String status = jsonObject.getString("status");
            if(status.equals("sold_out")||status.equals("trading")){
                //删除失效收藏
                return AjaxResult.error("此商品已售");
            }
        }catch (Exception ignored){
            //处理掉
            return AjaxResult.error("此商品已售");
        }*/

        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodCode",merOrder.getGoodCode());
        jsonObject.put("orderid",orderid);
        jsonArray.add(jsonObject);


        merOrderService.add(nickName,merOrder.getGoodCode(),merOrder.getTitle(),merOrder.getImageurl(),merOrder.getPrice(),
                merOrder.getCount(),merOrder.getUserid(),orderid,merOrder.getAddressid());
        return AjaxResult.success(PayUtil.createOdermr("日代拍商品下单",total,user.getOpenid(),user.getUserid(),"M",jsonArray));
    }


    @PostMapping("/payCart")
    @ApiOperation("煤炉购物车下单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商品主图url地址", name = "imageurl",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品编号", name = "goodCode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "商品标题", name = "title",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "价格", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品数量", name = "count",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "地址ID", name = "addressid",paramType = "query",dataType = "int"),
    })
    public AjaxResult payCart(@RequestBody JSONObject jsonObject) throws Exception {

        BigDecimal cartTotal = new BigDecimal(0);
        //String attachType = "1:";
        //根据用户ID查询openid
        User user = userService.selectByid(jsonObject.getInteger("userid"));
        String nickName = user.getNickName();
        Integer address = jsonObject.getInteger("addressid");
        //遍历购物车ID
        JSONArray shppingCart = jsonObject.getJSONArray("shppingCart");

        JSONArray jsonArray = new JSONArray();

        for (int i=0;i<shppingCart.size();i++){
            JSONObject shpping = shppingCart.getJSONObject(i);
            //依次获取购物车信息  和 下单要上传的数量  并且下单
            String goodCode = shpping.getString("goodCode");

            /*try {
                JSONObject jsonObject1 = getMecariDetail.notice(goodCode);
                String status = jsonObject1.getString("status");
                if(status.equals("sold_out")||status.equals("trading")){
                    //删除失效收藏
                    return AjaxResult.error("订单价格刷新，返回重试");
                }
            }catch (Exception ignored){
                //处理掉
                return AjaxResult.error("订单价格刷新，返回重试");
            }*/

            String title = shpping.getString("title");
            String imageurl = shpping.getString("imageurl");
            Integer count = shpping.getInteger("count");
            BigDecimal price = shpping.getBigDecimal("price");
            BigDecimal firstPayPrice = shpping.getBigDecimal("firstPayPrice");

            BigDecimal counts = new BigDecimal(count);
            BigDecimal total = firstPayPrice.multiply(counts);
            //统计总金额
            String orderid = OrderNoUtil.getOrderNo();

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("goodCode",goodCode);
            jsonObject1.put("orderid",orderid);
            jsonArray.add(jsonObject1);

            //attachType = attachType + orderid +":";
            cartTotal = cartTotal.add(total);
            //先创建订单
            merOrderService.add(nickName,goodCode,title,imageurl,price,
                    count,user.getUserid(),orderid,address);
        }




        return AjaxResult.success(PayUtil.createOdermr("代代米商品下单",cartTotal,user.getOpenid(),user.getUserid(),"M",jsonArray));
    }

    @PostMapping("/refundsOder")
    @ApiOperation("用户退款")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "退款金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "支付单号", name = "transactionid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单号", name = "orderid",paramType = "query",dataType = "string"),
    })
    public AjaxResult refundsOder(@RequestBody JSONObject jsonObject) throws Exception {

        BigDecimal price   = jsonObject.getBigDecimal("price");
        String transaction_id = jsonObject.getString("transactionid");
        String out_refund_no = jsonObject.getString("orderid");

        return AjaxResult.success(PayUtil.refundsOder("用户退款",price,transaction_id,out_refund_no));

    }



    @PostMapping("/payFinalPrice")
    @ApiOperation("用户付尾款")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "尾款总金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "主订单自增id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult payFinalPrice(@RequestBody JSONObject jsonObject) throws Exception {
        String attachType = "2:";
        Integer userid = jsonObject.getInteger("userid");
        User user = userService.selectByid(userid);
        Integer id = jsonObject.getInteger("id");
        BigDecimal price = jsonObject.getBigDecimal("price");
        //计算下总尾款
        MerOrder merOrder = merOrderService.selectByid(id);


        BigDecimal totalPrice = merOrder.getDgPrice().add(merOrder.getJpyunfei().add(merOrder.getGjyunfei().add(merOrder.getCangcufei().add(merOrder.getBaozhuangfei().add(merOrder.getOtherPrice())))));

        totalPrice = totalPrice.subtract(price);

        /*if(totalPrice.compareTo(BigDecimal.ZERO)!=0){
            return AjaxResult.error("尾款金额不一致");
        }*/
        attachType = attachType+id;

        List<String> list = new ArrayList<>();

        return AjaxResult.success(PayUtil.createOderRen("日代拍代购尾款",price,user.getOpenid(),userid,"X",list));
    }





    @PostMapping("/sendGood")
    @ApiOperation("管理员发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "物流公司", name = "wlcompany",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "物理单号", name = "trackingNumber",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "备注", name = "wlRemarks",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "订单id", name = "orderid",paramType = "query",dataType = "String"),
    })
    public AjaxResult sendGood(@RequestHeader("token") String token, @RequestBody JSONObject jsonObject) throws Exception {


        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        String wlcompany = jsonObject.getString("wlcompany");
        String trackingNumber = jsonObject.getString("trackingNumber");
        String wlRemarks = jsonObject.getString("wlRemarks");
        String orderid = jsonObject.getString("orderid");
        Integer userid = jsonObject.getInteger("userid");
       //修改订单状态   根据主id 先修改主订单   物流信息
        merOrderService.updataWlnew(wlcompany,trackingNumber,wlRemarks,orderid,userid);
        //查询主订单  取出自增id
        MerOrder merOrder = merOrderService.selectMyorderMain(orderid,userid);

        // 遍历子订单   修改子订单   物流信息
        List<MerOrder> merOrderList = merOrderService.selectByParentid(merOrder.getId());
        for (int i=0;i<merOrderList.size();i++){
            merOrderService.updataWl(wlcompany,trackingNumber,wlRemarks,merOrderList.get(i).getId(),userid);
        }
        return  AjaxResult.success("物流信息更新成功");
    }



    @PostMapping("/SelectOrderByType")
    @ApiOperation("根据用户id和状态查询订单 0全部订单,1待入库,2已入库，3申请发货（合并订单中）,4待付尾款，5、代发货6已发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult SelectOrderByType(@RequestBody JSONObject jsonObject) {

        return AjaxResult.success(merOrderService.selectMyorder(jsonObject.getByte("type"),jsonObject.getInteger("userid")));

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

        return AjaxResult.success(merOrderService.createmergeOrder(jsonObject));

    }



    @PostMapping("/updataTypeByid")
    @ApiOperation("根据用户id和type状态修改订单状态 后台修改入库时候使用  其他状态单独接口  1待入库，2已入库，3，申请发货状态")
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
        return AjaxResult.success(merOrderService.updataTypeByid(type,id,userid));

    }


    @PostMapping("/updataTypeByorderid")
    @ApiOperation("根据用户订单号orderid和type状态修改订单状态 后台修改入库时候使用  其他状态单独接口  1待入库，2已入库，3，申请发货状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "订单号", name = "orderid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataTypeByorderid(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {

        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }

        Integer userid = jsonObject.getInteger("userid");
        String orderid = jsonObject.getString("orderid");
        Byte type = jsonObject.getByte("type");
        return AjaxResult.success(merOrderService.updataTypeByorderid(type,orderid,userid));

    }




    @PostMapping("/updataPhoneTypeByorderid")
    @ApiOperation("前台根据用户订单号orderid和type状态修改订单状态 后台修改入库时候使用  其他状态单独接口  1待入库，2已入库，3，申请发货状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单状态", name = "type",paramType = "query",dataType = "byte"),
            @ApiImplicitParam(value = "订单号", name = "orderid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "首次支付金额(首次付款传  其他不传)", name = "firstPayPrice",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataPhoneTypeByorderid(@RequestBody JSONObject jsonObject) {

        Integer userid = jsonObject.getInteger("userid");
        String orderid = jsonObject.getString("orderid");
        Byte type = jsonObject.getByte("type");

        Byte xdtype = 1;

        if(type==xdtype){
            //查询用户id   修改自身消费金额   add
            User user = userService.selectByid(userid);
            BigDecimal myxfprice = user.getMyxfprice();
            MerOrder merOrder = merOrderService.selectByorderid(orderid);
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
            //merTbService.xd(merOrder.getGoodCode());

            BigDecimal firstPayPrice = jsonObject.getBigDecimal("firstPayPrice");
            return AjaxResult.success(merOrderService.updataTypeByorderids(type,firstPayPrice,orderid,userid));

        }else {
            return AjaxResult.success(merOrderService.updataTypeByorderid(type,orderid,userid));
        }

    }



    @PostMapping("/SelectAll")
    @ApiOperation("分页查询所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码，0表示第一页", name = "pageNumber",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "每页结果数", name = "pageSize",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "用户名", name = "nickName",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单编号", name = "orderid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "订单类型  0全部订单,1待入库,2已入库，3申请发货（合并订单中）,4待付尾款，5、代发货6已发货", name = "type",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "商品名称模糊查询", name = "title",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "（订单创建时间）开始时间", name = "begintime",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "（订单创建时间）结束时间", name = "endtime",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "商品code", name = "goodCode",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "首付款 空查全部 1有值 0没值", name = "isHaveFirstPayPrice",paramType = "query",dataType = "string"),
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

        String isHaveFirstPayPrice = jsonObject.getString("isHaveFirstPayPrice");

        return AjaxResult.success(merOrderService.selectAll(pageNumber,pageSize,nickName,orderid,type,title,begintime,endtime,userid,goodCode,isHaveFirstPayPrice));

    }


    //管理员入库操作

    @PostMapping("/updataFy")
    @ApiOperation("管理员计算费用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代购费用", name = "dgPrice",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "日本运费", name = "jpyunfei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "国际运费", name = "gjyunfei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "仓储费用", name = "cangcufei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "包装费用", name = "baozhuangfei",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "其他费用", name = "otherPrice",paramType = "query",dataType = "BigDecimal"),
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
        Integer id = jsonObject.getInteger("id");
        Integer userid = jsonObject.getInteger("userid");
        if(merOrderService.updataFy(dgPrice,jpyunfei,gjyunfei,cangcufei,baozhuangfei,otherPrice,otherPriceContent,id,userid)==0){
            return AjaxResult.error("更新失败");
        }
        return AjaxResult.success("更新成功");

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
        return AjaxResult.success(merOrderService.selectByOrderid(userid,id,addressid));

    }


    @PostMapping("/exportOrder")
    @ApiOperation("导出煤炉订单")
    public AjaxResult exportUser(HttpServletResponse response, @RequestBody JSONObject jsonData) throws Exception {
        //最终转出json
        JSONArray exportJson  = new JSONArray();

        List<MrOrderExport> list = new ArrayList<>();
        JSONObject jsonObject = jsonData;
        JSONArray jsonArray  = jsonObject.getJSONArray("list");
        //先给单个订单付值   循环便利    主订单导出
        for(int i=0;i<jsonArray.size();i++){
            //主订单
            MrOrderExport orderExportParent = new MrOrderExport();
            JSONObject orderParentjs = jsonArray.getJSONObject(i);
            orderExportParent.setOrderType("主订单");
            orderExportParent.setOrderid(orderParentjs.getString("orderid"));
            orderExportParent.setGoodCode(orderParentjs.getString("goodCode"));
            orderExportParent.setTitle(orderParentjs.getString("title"));
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
                MrOrderExport orderExportchildren = new MrOrderExport();

                orderExportchildren.setOrderType("子订单");
                orderExportchildren.setOrderid(orderChildjs.getString("orderid"));
                //log.info(orderChildjs.getString("orderid"));
                orderExportchildren.setGoodCode(orderChildjs.getString("goodCode"));
                orderExportchildren.setTitle(orderParentjs.getString("title"));
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
        list = JSONObject.parseArray(exportJson.toString(),MrOrderExport.class);


        excelService.exportExhibitors(response,list);
        //ExcelUtil<OrderExport> util = new ExcelUtil<OrderExport>(OrderExport.class);
        return null;
    }


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



    @PostMapping("/deleteByid")
    @ApiOperation("根据订单自增主id删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单自增主id", name = "id",paramType = "query",dataType = "int"),
    })
    public AjaxResult deleteByid(@RequestBody JSONObject jsonObject) {
        Integer id = jsonObject.getInteger("id");
        return merOrderService.deleteByid(id);

    }


/*    @PostMapping("cs")
    @ApiOperation("测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "=地址", name = "pay_url",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "请求方式", name = "method",paramType = "query",dataType = "String"),
    })
    public AjaxResult cs(@RequestBody MerOrder merOrder) throws IOException {

        return AjaxResult.success(merTbService.xd(merOrder.getGoodCode()));
        //AjaxResult.success(csPsot.post("https://api.mercari.jp/transactions/buy","buyer_id=541829980&consume_funds=0&consume_point=0&consume_docomo_point=0&consume_sales=0&payment_use_last_card=true&payment_amount=300&payment_fee=0&pay_times=1&pay_method=1&seller_id=167020036&item_id=m44623955140&item_name=d%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%A0+d+program%E7%AD%89%E3%80%80%E8%A9%A6%E4%BE%9B%E5%93%81%E3%82%BB%E3%83%83%E3%83%88&price=300&description=%E7%A8%AE%E9%A1%9E%C2%B7%C2%B7%C2%B7%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%2F%E8%A9%A6%E4%BE%9B%E5%93%81%0A%0A%EF%BD%84%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%A0%0A%E3%83%A2%E3%82%A4%E3%82%B9%E3%83%88%E3%82%B1%E3%82%A2%E3%80%80%E3%82%A8%E3%83%9E%E3%83%AB%E3%82%B8%E3%83%A7%E3%83%B3%E3%80%80MB+%C3%97+1%0A%0A%E3%83%A2%E3%82%A4%E3%82%B9%E3%83%88%E3%82%B1%E3%82%A2%E3%80%80%E3%83%AD%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E3%80%80MB+%C3%97+1%0A%0A%E3%82%AB%E3%83%B3%E3%83%80%E3%83%B3%E3%83%90%E3%83%AA%E3%82%A2%E3%80%80%E3%82%A8%E3%83%83%E3%82%BB%E3%83%B3%E3%82%B9%E3%80%80%C3%97+3%0A%E3%80%80%0A%E3%81%8A%E3%81%BE%E3%81%91%E3%81%A7RMK%E3%83%AA%E3%82%AF%E3%82%A4%E3%83%89%E3%83%95%E3%82%A1%E3%83%B3%E3%83%87%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%C3%972%E3%81%8A%E4%BB%98%E3%81%91%E3%81%97%E3%81%BE%E3%81%99%E3%80%82%0A%0A%E3%81%A9%E3%81%86%E3%81%9E%E3%82%88%E3%82%8D%E3%81%97%E3%81%8F%E3%81%8A%E9%A1%98%E3%81%84%E3%81%84%E3%81%9F%E3%81%97%E3%81%BE%E3%81%99%E3%80%82%0A%0A%0A%E7%A8%AE%E9%A1%9E%C2%B7%C2%B7%C2%B7%E7%BE%8E%E5%AE%B9%E6%B6%B2%2F%E3%82%BB%E3%83%A9%E3%83%A0&brand_name=3139&category_id=1266&item_condition=1&shipping_payer=2&shipping_method=5&shipping_duration=2&shipping_from_area=99&shipping_class=0&_checksum=2893e146e51946677daf0b3a38716b4f&id=73696261&zip_code1=557&zip_code2=0061&family_name=%E5%AE%87%E5%A5%87&first_name=%E5%9B%BD%E9%9A%9B&family_name_kana=%E3%82%A6%E3%82%AD&first_name_kana=%E3%82%B3%E3%82%AF%E3%82%B5%E3%82%A4&prefecture=%E5%A4%A7%E9%98%AA%E5%BA%9C&city=%E5%A4%A7%E9%98%AA%E5%B8%82%E8%A5%BF%E6%88%90%E5%8C%BA&address1=%E5%8C%97%E6%B4%A5%E5%AE%884-4-75-47&address2=&telephone=08068048053&state_abbreviation=%E5%A4%A7%E9%98%AA%E5%BA%9C&purchase_session_id=57c04cb0557623860d1be4f8e9f53bdc&ship_to_address_id=73696261&paid_method=deferred_payment"));
    }*/



}
