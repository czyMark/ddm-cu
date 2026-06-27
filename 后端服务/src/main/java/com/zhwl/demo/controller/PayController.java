
package com.zhwl.demo.controller;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.po.*;
import com.zhwl.demo.service.*;
import com.zhwl.demo.utils.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author txq
 * @Date 2021/11/14 15:15
 * @Version 1.0
 */

@RestController
@RequestMapping("/pay")
@Api(tags = "支付模块")
@Slf4j
@Validated
@CrossOrigin//跨域：A域名访问B域名   域名或者请求端口 或者协议不一致
public class PayController {
    @Autowired
    private UserService userService;

    @Autowired
    private MerTbService merTbService;

    @Autowired
    private BalanceUpdataService balanceUpdata;

    @Autowired
    private MerOrderService merOrderService;

    @Autowired
    private GoodOrderService goodOrderService;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private MrShppingCartService mrShppingCartService;

    @Autowired
    private DepositService depositService;

    @PostMapping("cs")
    @ApiOperation("测试支付")
    public JSONObject createOrder(@RequestBody JSONObject jsonObjectp)throws Exception{

        String amount = jsonObjectp.getString("amount");
        String desc = jsonObjectp.getString("desc");
        String openid = jsonObjectp.getString("openid");

        //先获取token
        String re2 = HttpRequest.get("https://api.asiapayjp.com/mch-api/v1/auth?grant_type=client_credentials&uid=YOUR_UID&client_secret=YOUR_CLIENT_SECRET")
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(re2);
        String access_token = jsonObject.getString("access_token");
        log.info(access_token);

        IdWorker idWorker = new IdWorker(0,0);
        long order_codes=idWorker.nextId();
        String out_trade_no = "c"+ String.valueOf(order_codes);

        String body = "{\n" +
                "  \"payment_method\": \"MINI_PROGRAM\",\n" +
                "  \"merchant_order_id\": \""+out_trade_no+"\",\n" +
                "  \"order_amount\": \""+amount+"\",\n" +
                "  \"order_currency\": \"JPY\",\n" +
                "  \"order_description\": \""+desc+"\",\n" +
                "  \"sub_appid\": \"wx057f2980ef386db6\",\n" +
                "  \"sub_openid\": \""+openid+"\",\n" +
                "  \"notify_url\": \"https://ddm-cu.com//pay/callback\",\n" +
                "  \"expired_time\": 3000\n" +
                "}";


        log.info(body);


        String payresult = HttpRequest.post("https://api.asiapayjp.com/mch-api/v1/acq/wechat/create_payment")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+access_token)
                .body(body)
                .execute().body();
        log.info(payresult);
        JSONObject jsonObject1 = JSONObject.parseObject(payresult);


        return jsonObject1;
        //return PayUtil.createOder(desc,amount,openid,"0");
    }
    //https://www.test.com/pay/callback



    @PostMapping("refontback")
    public AjaxResult refontback(HttpServletRequest request) throws IOException {
        System.out.println("Signature"+request.getHeader("Signature"));
        String Signature = request.getHeader("Signature");

        BufferedReader reader = request.getReader();
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        String bodyContent = content.toString();
        System.out.println(bodyContent);
        /*String realSignature = getSHA256(bodyContent+".6b89ae80cfc44e5c99dcca82f2dba570");
        System.out.println("realSignature:"+realSignature);
        if(Signature.equals(realSignature)) {

        }*/
        return AjaxResult.success();
    }


    @PostMapping("callback")
    public AjaxResult callback(HttpServletRequest request) throws IOException {
        System.out.println("Signature"+request.getHeader("Signature"));
        String Signature = request.getHeader("Signature");

        BufferedReader reader = request.getReader();
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        String bodyContent = content.toString();
        System.out.println(bodyContent);
        String realSignature = getSHA256(bodyContent+".6b89ae80cfc44e5c99dcca82f2dba570");
        System.out.println("realSignature:"+realSignature);
        if(Signature.equals(realSignature)) {

            JSONObject jsonObject = JSONObject.parseObject(bodyContent);
            JSONObject data = jsonObject.getJSONObject("data");
            String order_amount = data.getString("order_amount");
            String merchant_order_id = data.getString("merchant_order_id");

            //处理结果
            if (merchant_order_id.contains("Y")) {
                //用户充值押金
                String userid = merchant_order_id.split("Y")[0];
                //充值用户押金
                BigDecimal jpBalance = new BigDecimal(order_amount);

                BigDecimal sl = new BigDecimal(1.01);

                BigDecimal zsBalance = jpBalance.divide(sl,0,RoundingMode.UP);

                //根据用户id  查询用户信息
                User user = userService.selectByid(Integer.valueOf(userid));
                //根据用户openid更新充值金额
                userService.payYj(zsBalance, user.getOpenid());
                //增加充值记录
                depositService.Add(Integer.valueOf(userid), zsBalance, merchant_order_id);
            }


            if (merchant_order_id.contains("C")) {
                String userid = merchant_order_id.split("C")[0];

                //更新用户充值
                BigDecimal jpBalance = new BigDecimal(order_amount);

                BigDecimal sl = new BigDecimal(1.01);

                BigDecimal zsBalance = jpBalance.divide(sl,0,RoundingMode.UP);

                User user = userService.selectByid(Integer.valueOf(userid));

                //根据用户openid更新充值金额
                userService.pay(zsBalance, user.getOpenid());

                Byte type = 1;
                //新增用户余额变更记录
                balanceUpdata.Add(zsBalance, type, "充值", Integer.valueOf(userid));
            }


            //以上结束


            //取出订单号  然后去判断下  是什么类型  对应去更改状态



        /*Map result = new HashMap();

        result.put("code", "FAIL");

        //拼接回调json通知并输出

        try {
            StringBuilder signStr = new StringBuilder();
            //应答时间戳\n
            signStr.append(request.getHeader("Wechatpay-Timestamp")).append("\n");
            //应答随机串\n
            signStr.append(request.getHeader("Wechatpay-Nonce")).append("\n");
            //应答报文主体\n
            BufferedReader bufferedReader = request.getReader();
            String str = null;
            StringBuilder builder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
            System.out.println(builder);
            signStr.append(builder.toString()).append("\n");

            //验证签名
            if (!PayUtil.signVerify(request.getHeader("Wechatpay-Serial"),signStr.toString(),request.getHeader("Wechatpay-Signature"))){
                result.put("meassage","sign error");
                return  AjaxResult.error("sigin错误",result);
            }

            //解密密文
            System.out.println(PayUtil.decryptOrder(builder.toString()));
            //将密文转化为字符串
            String hd = PayUtil.decryptOrder(builder.toString());

            //log.info(hd+"解密后的字符串");

            //转化为jason
            JSONObject jsonObject = JSONObject.parseObject(hd);
            //获取某个字段的值
            String transaction_id = jsonObject.getString("transaction_id");
            String out_trade_no = jsonObject.getString("out_trade_no");
            String attachType = jsonObject.getString("attach");
            String openid = jsonObject.getJSONObject("payer").getString("openid");
            BigDecimal bai = new BigDecimal(100);
            BigDecimal balance = jsonObject.getJSONObject("amount").getBigDecimal("payer_total");
            balance = balance.divide(bai);
*/


            /*String[] orderidList = attachType.split(":");


            if(orderidList[0].equals("0")){
                //更新用户充值
                BigDecimal jpBalance = new BigDecimal(orderidList[1]);
                //根据用户openid更新充值金额
                userService.pay(jpBalance,openid);

                Integer userid = userService.selectByopenid(openid).getUserid();
                Byte type = 1;
                //新增用户余额变更记录
                balanceUpdata.Add(jpBalance, type,"充值",userid);
            }else {
                //获取参数之后   分切字符串

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

                }else {
                    //处理尾款回调
                    if(orderidList[0].equals("2")){
                        //先获取主订单
                        MerOrder merOrder = merOrderService.selectByid(Integer.valueOf(orderidList[1]));
                        //更新订单状态  代发货
                        Byte type = 5;
                        merOrderService.updataTypeByid(type,merOrder.getId(),merOrder.getUserid());
                        //获取所有子订单id   去更新状态
                        List<MerOrder> merOrderList = merOrderService.selectByParentid(merOrder.getId());
                        for (int i=0;i<merOrderList.size();i++){
                            merOrderService.updataTypeByid(type,merOrderList.get(i).getId(),merOrderList.get(i).getUserid());
                        }

                    }else {
                        if(orderidList[0].equals("3")){
                            //充值用户押金
                            BigDecimal jpBalance = new BigDecimal(orderidList[1]);
                            //根据用户openid更新充值金额
                            userService.payYj(jpBalance,openid);

                            Integer userid = userService.selectByopenid(openid).getUserid();
                            //增加充值记录
                            depositService.Add(userid,balance,transaction_id);
                        }
                        if(orderidList[0].equals("4")){
                            //更新订单状态
                            Integer id = Integer.parseInt(orderidList[1]);
                            Integer userid = Integer.parseInt(orderidList[2]);
                            Byte type = 8;
                            goodOrderService.updataTypeByid(type,id,userid);
                            //遍历子订单 更改子订单状态
                            List<GoodOrder> goodOrderList = goodOrderService.selectByParentid(id);
                            for(int i=0;i<goodOrderList.size();i++){
                                goodOrderService.updataTypeByid(type,goodOrderList.get(i).getId(),userid);
                            }

                        }
                        if(orderidList[0].equals("5")){
                            //更新订单状态
                            Integer id = Integer.parseInt(orderidList[1]);
                            Integer userid = Integer.parseInt(orderidList[2]);
                            Byte type = 4;
                            goodOrderService.updataTypeByid(type,id,userid);
                            goodOrderService.updatapaytime(id);

                            //遍历子订单 更改子订单状态
                            *//*List<GoodOrder> goodOrderList = goodOrderService.selectByParentid(id);
                            for(int i=0;i<goodOrderList.size();i++){
                                goodOrderService.updataTypeByid(type,goodOrderList.get(i).getId(),userid);
                            }*//*

                        }

                    }

                }


            }*/


            //orderService.updataCode(out_trade_no,transaction_id );

           /* System.out.println(transaction_id);

            //验证订单
            result.put("code", "SUCCESS");
            result.put("transaction_id", transaction_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.success(result);*/
        }
        return null;
    }


    public static String getSHA256(String input) {
        try {
            // 获取MessageDigest类的实例，使用SHA-256算法
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // 使用指定的字节更新摘要
            md.update(input.getBytes());
            // 完成哈希计算，得到结果
            byte[] digest = md.digest();
            // 将字节转换为十六进制表示形式
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // 如果找不到算法，抛出运行时异常
        }
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }







/*    @PostMapping("refundsOder")
    public Map refundsOder(String desc, BigDecimal amount,String transaction_id,String out_refund_no) throws Exception{
        return PayUtil.refundsOder(desc,amount,transaction_id,out_refund_no);
    }*/

    @PostMapping("refundCall")
    public Map refundCall(HttpServletRequest request) {
        System.out.println("Wechatpay-Timestamp"+request.getHeader("Wechatpay-Timestamp"));
        System.out.println("Wechatpay-Nonce"+request.getHeader("Wechatpay-Nonce"));
        System.out.println("Wechatpay-Signature"+request.getHeader("Wechatpay-Signature"));
        System.out.println("Wechatpay-Serial"+request.getHeader("Wechatpay-Serial"));

        Map result = new HashMap();
        result.put("code", "FAIL");

        //拼接回调json通知并输出

        try {
            StringBuilder signStr = new StringBuilder();
            //应答时间戳\n
            signStr.append(request.getHeader("Wechatpay-Timestamp")).append("\n");
            //应答随机串\n
            signStr.append(request.getHeader("Wechatpay-Nonce")).append("\n");
            //应答报文主体\n
            BufferedReader bufferedReader = request.getReader();
            String str = null;
            StringBuilder builder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
            System.out.println(builder);
            signStr.append(builder.toString()).append("\n");

            //验证签名
            if (!PayUtil.signVerify(request.getHeader("Wechatpay-Serial"),signStr.toString(),request.getHeader("Wechatpay-Signature"))){
                result.put("meassage","sign error");
                return  result;
            }

            //解密密文
            System.out.println(PayUtil.decryptOrder(builder.toString()));
            //将密文转化为字符串
            String hd = PayUtil.decryptOrder(builder.toString());
            //转化为jason
            JSONObject jsonObject = JSONObject.parseObject(hd);
            //获取某个字段的值
            String transaction_id = jsonObject.getString("transaction_id");
            String out_trade_no = jsonObject.getString("out_refund_no");


           MerOrder  merOrderList = merOrderService.selectBytransaction_id(transaction_id);
           if(merOrderList!=null){
               log.info("去退款");
               //煤炉下单退款
               Byte type = 7;
               //根据订单号更新  用户状态已经退款
               merOrderService.updataType(type,out_trade_no);
           }else {
               Deposit deposits = depositService.selectVBytransaction_id(transaction_id);
               if(deposits!=null){
                   //押金充值退款回调
                   //根据支付单号  查询  transaction_id  充值记录  获取用户id
                   Integer userid = deposits.getUserid();
                   //根据openid  解冻押金
                   userService.jiedong(userid,deposits.getBalance());
                   //变更押金充值
                   depositService.updataType(transaction_id);
               }
           }

            //验证订单
            result.put("code", "SUCCESS");
            result.put("transaction_id", transaction_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
