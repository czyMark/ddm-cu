package com.zhwl.demo.controller;

import cn.hutool.db.Page;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.BalanceUpdata;
import com.zhwl.demo.po.PayBalance;
import com.zhwl.demo.po.PayUtil;
import com.zhwl.demo.po.User;
import com.zhwl.demo.service.*;
import com.zhwl.demo.utils.IdWorker;
import com.zhwl.demo.utils.OrderNoUtil;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author txq
 * @Date 2023/7/31 0:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/account")
@Api(tags = "用户账户模块")
@Slf4j
@Validated
public class UserAccountController {

    @Autowired
    private UserAccountService accountService;

    @Autowired
    private MerOrderService merOrderService;

    @Autowired
    private GetMecariDetail getMecariDetail;


    @Autowired
    private BalanceUpdataService balanceUpdata;

    @Autowired
    private BalanceUpdataService balanceUpdataService;

    @Autowired
    private GoodOrderService goodOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepositService depositService;

    @PostMapping("/selectUserBalance")
    @ApiOperation("查询用户余额")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
    })
    public AjaxResult selectAll(@RequestBody User user) {
        Integer userid = user.getUserid();
        HashMap balance = new HashMap();
        balance.put("balance",accountService.totalBalance(user.getUserid()));
        balance.put("yjbalance",accountService.yjBalance(user.getUserid()));
        BigDecimal myxfprice = goodOrderService.selectxf(userid);
        if(myxfprice == null){
            myxfprice = new BigDecimal(0);
        }
        balance.put("myxfprice",myxfprice);
        return AjaxResult.success(balance);
    }

    @PostMapping("/selectUserBlUpdata")
    @ApiOperation("根据用户id和变更状态查询变更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "状态 0全部 1充值 2消费 3提现 4解冻", name = "type",paramType = "query",dataType = "int"),
    })
    public AjaxResult selectUserBlUpdata(@RequestBody BalanceUpdata balanceUpdata) {
        return AjaxResult.success(balanceUpdataService.selectByuserid(balanceUpdata.getUserid(),balanceUpdata.getType()));
    }

    @PostMapping("/payBalance")
    @ApiOperation("用户充值余额模块")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "充值金额（日元）", name = "jpbalance",paramType = "query",dataType = "BigDecimal"),

    })
    public AjaxResult payBalance(@RequestBody PayBalance payBalance) throws Exception {
        //查询用户openid
        User user = userService.selectByid(payBalance.getUserid());

        if(payBalance.getJpbalance()==null){
            return AjaxResult.error("充值金额不能空");
        }

        List<String> list  = new ArrayList<>();

        //BigDecimal sl = new BigDecimal(1.01);
        BigDecimal jpbalance = payBalance.getJpbalance();
        //jpbalance = jpbalance.multiply(sl);

        //拼接传参
        return  AjaxResult.success(PayUtil.createOder("用户充值",jpbalance,user.getOpenid(),payBalance.getUserid(),"C",list));
    }










    @PostMapping("/useBalance")
    @ApiOperation("用户余额消费模块")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "消费金额）", name = "balance",paramType = "query",dataType = "BigDecimal"),
    })
    public AjaxResult useBalance(@RequestBody PayBalance payBalance) throws Exception {
        //查询用户openid
        User user = userService.selectByid(payBalance.getUserid());

        if(payBalance.getBalance()==null){
            return AjaxResult.error("充值金额不能空");
        }


        if(payBalance.getBalance().compareTo(user.getBalance())>0){
            return AjaxResult.error("余额不足");
        }

        BigDecimal yuebalance = user.getBalance().subtract(payBalance.getBalance());

        //更新用户余额
        accountService.usebalance(yuebalance,user.getUserid());
        Byte type = 2;
        //新增余额变更记录
        balanceUpdata.Add(payBalance.getBalance(), type, "消费", user.getUserid());

        //拼接传参
        return  AjaxResult.success("余额付款成功");
    }




    @PostMapping("/useMrBalance")
    @ApiOperation("用户余额煤炉消费模块")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "消费金额）", name = "balance",paramType = "query",dataType = "BigDecimal"),
    })
    public AjaxResult useMrBalance(@RequestBody JSONObject jsonObject) throws Exception {
        BigDecimal cartTotal = new BigDecimal(0);
        //根据用户ID查询openid
        User user = userService.selectByid(jsonObject.getInteger("userid"));
        String nickName = user.getNickName();
        Integer address = jsonObject.getInteger("addressid");
        //遍历购物车ID
        JSONArray shppingCart = jsonObject.getJSONArray("shppingCart");

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < shppingCart.size(); i++) {
            JSONObject shpping = shppingCart.getJSONObject(i);
            //依次获取购物车信息  和 下单要上传的数量  并且下单
            String goodCode = shpping.getString("goodCode");

            /*try {
                JSONObject jsonObject1 = getMecariDetail.notice(goodCode);
                String status = jsonObject1.getString("status");
                if (status.equals("sold_out") || status.equals("trading")) {
                    //删除失效收藏
                    return AjaxResult.error("订单价格刷新，返回重试");
                }
            } catch (Exception ignored) {
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
            cartTotal = cartTotal.add(total);

            //先创建订单
            merOrderService.add(nickName, goodCode, title, imageurl, price,
                    count, user.getUserid(), orderid, address);
            //拼接传参
        }

        //总付款金额   去掉
        if(cartTotal.compareTo(user.getBalance())>0){
            return AjaxResult.error("余额不足");
        }
        BigDecimal yuebalance = user.getBalance().subtract(cartTotal);
        //更新用户余额
        accountService.usebalance(yuebalance,user.getUserid());
        Byte type = 2;
        //新增余额变更记录
        balanceUpdata.Add(cartTotal, type, "消费", user.getUserid());

        return AjaxResult.success("余额付款成功",jsonArray);
    }


    @PostMapping("/payYjBalance")
    @ApiOperation("用户充值押金模块")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            //@ApiImplicitParam(value = "需支付的押金金额（保存两位小数）", name = "balance",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "充值押金金额（日元）", name = "jpbalance",paramType = "query",dataType = "BigDecimal"),

    })
    public AjaxResult payYjBalance(@RequestBody JSONObject jsonObject) throws Exception {

        Integer userid =jsonObject.getInteger("userid");
        BigDecimal jpbalance = jsonObject.getBigDecimal("jpbalance");
        //查询用户openid
        User user = userService.selectByid(userid);

        if(jpbalance==null){
            return AjaxResult.error("充值金额不能空");
        }
        /*if(payBalance.getBalance()==null){
            return  AjaxResult.error("需支付金额不能为空");
        }*/
        //拼接传参

        Integer attachType = 1;

        List<String> list  = new ArrayList<>();

        //BigDecimal sl = new BigDecimal(1.01);
        //jpbalance //= jpbalance.multiply(sl);

        return  AjaxResult.success(PayUtil.createOder("押金充值",jpbalance,user.getOpenid(),userid,"Y",list));
    }


    @PostMapping("/updataYjBalance")
    @ApiOperation("根据押金记录id修改押金状态，0是已充值 ,1申请解冻,2已退款")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "自增id", name = "id",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "金额", name = "balance",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "状态", name = "type",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "BigDecimal"),
    })
    public AjaxResult updataYjBalance(@RequestBody JSONObject jsonObject) throws Exception {

       Integer id = jsonObject.getInteger("id");
       Integer type = jsonObject.getInteger("type");
       Integer userid = jsonObject.getInteger("userid");
       BigDecimal balance = jsonObject.getBigDecimal("balance");
       if(type ==2 ){
           //修改对应用户的  钱
           userService.jiedong(userid,balance);
       }
       depositService.updataTypeByid(id,type);
        return  AjaxResult.success("修改成功");
    }



   /* @PostMapping("/refundYj")
    @ApiOperation("用户退押金模块")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "支付单号", name = "transactionid",paramType = "query",dataType = "int"),
            //@ApiImplicitParam(value = "需支付的押金金额（保存两位小数）", name = "balance",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "退款金额（日元）", name = "jpbalance",paramType = "query",dataType = "BigDecimal"),

    })
    public AjaxResult create_refund(@RequestBody JSONObject jsonObject) throws Exception {

        //先获取token
        String re2 = HttpRequest.get("https://api.asiapayjp.com/mch-api/v1/auth?grant_type=client_credentials&uid=YOUR_UID&client_secret=YOUR_CLIENT_SECRET")
                .execute().body();
        JSONObject jsonObject1 = JSONObject.parseObject(re2);
        String access_token = jsonObject1.getString("access_token");

        String payment_order_id  = jsonObject.getString("transactionid");
        BigDecimal jpbalance = jsonObject.getBigDecimal("jpbalance");

        IdWorker idWorker = new IdWorker(0,0);
        long order_codes=idWorker.nextId();

        String body = "{\n" +
                "  \"merchant_payment_order_id\": \""+payment_order_id+"\",\n" +
                "  \"merchant_refund_id\": \""+order_codes+"\",\n" +
                "  \"refund_amount\": \""+String.valueOf(jpbalance)+"\",\n" +
                "  \"notify_url\": \"https://ddm-cu.com//pay/callback\",\n" +
                "  \"reason\": \"押金退款\",\n" +
                "}";

        String payresult = HttpRequest.post("https://api.asiapayjp.com/mch-api/v1/acq/wechat/create_refund")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+access_token)
                .body(body)
                .execute().body();
        log.info(payresult);
        //JSONObject jsonObject1 = JSONObject.parseObject(payresult);
        return  AjaxResult.success(payresult);
    }*/



    @PostMapping("/jdyj")
    @ApiOperation("用户申请解冻")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "支付单号", name = "transactionid",paramType = "query",dataType = "String"),
    })
    public AjaxResult jdyj(@RequestBody JSONObject jsonObject) throws Exception {
        String transactionid = jsonObject.getString("transactionid");
        Integer userid = jsonObject.getInteger("userid");
        return AjaxResult.success(depositService.updataTypeing(userid,transactionid));
    }


    //根据用户id查询押金列表
    @PostMapping("/yjList")
    @ApiOperation("根据用户id查询押金列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id(不传参查询所有)", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "押金状态，0已充值 1申请退款中，2已退款 3审核驳回（不传参查询所有）", name = "type",paramType = "query",dataType = "Byte"),
            @ApiImplicitParam(value = "页码，1表示第一页", name = "pageNumber",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "每页结果数", name = "pageSize",paramType = "query",dataType = "int"),
    })
    public AjaxResult yjList(@RequestBody JSONObject jsonObject) throws Exception {
        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Integer userid = jsonObject.getInteger("userid");
        Byte type = jsonObject.getByte("type");
        return AjaxResult.success(depositService.select(userid,type,pageNumber,pageSize));
    }

    @PostMapping("/yjShenpi")
    @ApiOperation("后台押金审批")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "退款金额", name = "price",paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(value = "支付单号", name = "transactionid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "0驳回  1通过", name = "type",paramType = "query",dataType = "int"),
    })
    public AjaxResult yjShenpi(@RequestBody JSONObject jsonObject) throws Exception {

        Integer type = jsonObject.getInteger("type");
        BigDecimal price = jsonObject.getBigDecimal("price");
        String transactionid = jsonObject.getString("transactionid");

        if(type==0){
            //驳回
            return AjaxResult.success(depositService.updataTypeErr(transactionid));
        }


        return depositService.jiedong(price,transactionid);
    }


}
