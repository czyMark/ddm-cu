package com.zhwl.demo.controller;
import cn.hutool.db.Page;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.config.RuoYiConfig;
import com.zhwl.demo.mapper.UserAddressMapper;
import com.zhwl.demo.po.*;
import com.zhwl.demo.service.UlaAddrdService;
import com.zhwl.demo.service.UserAddressService;
import com.zhwl.demo.service.UserService;
import com.zhwl.demo.service.impl.AesCbcUtil;
import com.zhwl.demo.utils.FileUploadUtils;
import com.zhwl.demo.utils.RedisUtils;
import com.zhwl.demo.utils.UUIDUtils;
import com.zhwl.demo.utils.WxSqHttpRequest;
import com.zhwl.demo.vo.LoginOutVo;
import com.zhwl.demo.vo.LoginVo;
import com.zhwl.demo.vo.SessionOutVo;
import com.zhwl.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;


/**
 * @Author txq
 * @Date 2023/7/11 19:59
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "微信登录调用后的code", name = "code",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "手机号授权调用后的code", name = "phonecode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "手机号", name = "loginphone",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "验证码", name = "phonecode",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "邀请码", name = "invitecode",paramType = "query",dataType = "String"),
    })
    public AjaxResult login(@RequestBody LoginCode loginCode) throws Exception {

        if(loginCode==null){
            return AjaxResult.error("code不能为空");
        }
        log.info("code:"+loginCode.getCode());
        //小程序唯一标识   (在微信小程序管理后台获取)+
        String wxspAppid = "your-wechat-appid";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "your-wechat-app-secret";
        //授权（必填）
        String grant_type = "authorization_code";
        //1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + loginCode.getCode() + "&grant_type=" + grant_type;
        //发送请求
        String sr = WxSqHttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        //log.info(json.toJSONString());
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");
        /*System.out.println(session_key);
        System.out.println("openid="+openid);*/
        //2、对encryptedData加密数据进行AES解密
        /*String userinfos = AesCbcUtil.decrypt(loginCode.getEncryptedData(), session_key, loginCode.getIv());
        JSONObject userinfo = JSONObject.parseObject(userinfos);
        //解析json    头像   昵称
        String nickName = userinfo.getString("nickName");
        String avatarUrl = userinfo.getString("avatarUrl");*/
        AjaxResult result = userService.login(openid,loginCode.getLoginphone(),loginCode.getInvitecode(),loginCode.getPhonecode());
        if (!AjaxResult.isSuccess(result)) return result;

        LoginOutVo outVo = (LoginOutVo) result.get("data");
        Integer id = outVo.getUserid();
        // 返回数据
        outVo.setSession(getSessionId(id));
        outVo.setUserinfo(outVo.getUserinfo());
        return AjaxResult.success("登录成功", outVo);
    }



    @PostMapping("/loginPhone")
    @ApiOperation("手机号授权登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "微信登录调用后的code", name = "code",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "用户的openid", name = "openid",paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "邀请码", name = "invitecode",paramType = "query",dataType = "String"),
    })
    public AjaxResult loginPhone(@RequestBody JSONObject jsonObject) throws Exception {

        if(!jsonObject.containsKey("code")){
            return AjaxResult.error("code不能为空");
        }
        String openid = jsonObject.getString("openid");

        //小程序唯一标识   (在微信小程序管理后台获取)+
        String wxspAppid = "your-wechat-appid";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "your-wechat-app-secret";
        //授权（必填）
        String grant_type = "authorization_code";
        //1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //请求参数
        //String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + loginCode.getCode() + "&grant_type=" + grant_type;
        String params = "grant_type=client_credential&appid=" + wxspAppid + "&secret="+ wxspSecret;


        //发送请求
        String sr = WxSqHttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);

        //log.info(sr);

        JSONObject jsonObject1 = JSONObject.parseObject(sr);
        String access_token = jsonObject1.getString("access_token");

        String body = "{\n" +
                "\"code\": \""+jsonObject.getString("code")+"\"\n" +
                "}";

        //发送请求
        String re = HttpRequest.get("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token="+access_token)
                        .body(body)
                        .execute().body();
        log.info(re);
        JSONObject res = JSONObject.parseObject(re);
        JSONObject phone_info = res.getJSONObject("phone_info");
        String phoneNumber = phone_info.getString("phoneNumber");

        //根据openid  查询用户信息
        User user = userService.selectByopenid(openid);

        if(user.getLoginphone()==""){
            //更新手机号绑定
            if(jsonObject.containsKey("invitecode")){
                if(!Objects.equals(jsonObject.getString("invitecode"), "")){
                    //更新  用户手机号  和  邀请码
                    //查看一下用户是否已经有邀请码
                    if(user.getInvitecode()==""){

                         List<User> list = userService.selectBymyinvitecodes(jsonObject.getString("invitecode"));
                        if(list.size()==0){
                            return AjaxResult.error("邀请码不存在");
                        }
                        userService.updataPhoneandIn(phoneNumber,phoneNumber,jsonObject.getString("invitecode"),openid);
                    }else {
                        //只更新手机号
                        userService.updataPhone(phoneNumber,phoneNumber,openid);
                    }

                }else {
                    //只更新手机号
                    userService.updataPhone(phoneNumber,phoneNumber,openid);
                }
            }
        }else {
            if(jsonObject.containsKey("invitecode")){
                if(!Objects.equals(jsonObject.getString("invitecode"), "")){
                    //更新  用户手机号  和  邀请码
                    if(user.getInvitecode()==""){
                        List<User> list = userService.selectBymyinvitecodes(jsonObject.getString("invitecode"));
                        if(list.size()==0){
                            return AjaxResult.error("邀请码不存在");
                        }
                        userService.updataPhoneandIn(phoneNumber,phoneNumber,jsonObject.getString("invitecode"),openid);
                    }
                }
            }
        }

        return AjaxResult.success("授权成功");//AjaxResult.success("登录成功", outVo);
    }






    @PostMapping("/selectzl")
    @ApiOperation("根据用户id查询个人资料")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
    })
    public AjaxResult selectzl(@RequestBody User user) {
       return userService.selectzl(user.getUserid());

    }

    @PostMapping("/selectMyxiaji")
    @ApiOperation("根据用户id查询下级用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
    })
    public AjaxResult selectMyxiaji(@RequestBody User user) throws JsonProcessingException {

//        ObjectMapper mapper = new ObjectMapper();
//
//
//        User user1 = userService.selectByid(user.getUserid());
//
//        BigDecimal totalxjprice = user1.getXjxfprice();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("totalxjprice",totalxjprice);
//        jsonObject.put("yjtxprice",user1.getYjtxprice());
//        List<User> users = userService.SelectSj(user1.getMyinvitecode());
//        jsonObject.put("list",users);
//
//
//        JSONArray jsonArray = jsonObject.getJSONArray("list");


        User user1 = userService.selectByid(user.getUserid());

        //根据用户id  查询素有下级  消费总金额
        BigDecimal total_amount = userService.total_amount(user1.getMyinvitecode());
        if(total_amount==null){
            total_amount = new BigDecimal(0);
        }
        String parentId = user1.getMyinvitecode(); // 根节点ID
        List<Map<String, Object>> allChildren = getChildrenRecursively(parentId);
        System.out.println(allChildren.size());
        String json = buildHierarchicalJson(allChildren);

        json = json.replaceAll("\\\\","");
        json = json.replace("\"[", "[");
        json = json.replace("]\"", "]");
        System.out.println(json);
        JSONArray jsonObject1 = JSONArray.parseArray(json);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalxjprice",total_amount);
        jsonObject.put("yjtxprice",user1.getYjtxprice());
        jsonObject.put("list",jsonObject1);


        return AjaxResult.success(jsonObject);
    }



    public List<Map<String, Object>> getChildrenRecursively(String myinvitecode) {
        System.out.println(myinvitecode);
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取当前层级的直接子节点
        List<User> users = userService.SelectSj(myinvitecode);
            for (int i=0;i<users.size();i++){
                //根据用户id  查询素有下级  消费总金额
                BigDecimal total_amount = userService.total_amount(users.get(i).getMyinvitecode());
                if(total_amount==null){
                    total_amount = new BigDecimal(0);
                }

                Map<String, Object> node = new HashMap<>();
                node.put("userid", users.get(i).getUserid());
                node.put("totalxjprice", total_amount);
                node.put("openid", users.get(i).getOpenid());
                node.put("account", users.get(i).getAccount());
                node.put("realname", users.get(i).getRealname());
                node.put("phone", users.get(i).getPhone());
                node.put("twophone", users.get(i).getTwophone());
                node.put("balance", users.get(i).getBalance());
                node.put("isRead", users.get(i).getIsRead());
                node.put("contact", users.get(i).getContact());
                node.put("nickName", users.get(i).getNickName());
                node.put("avatarUrl", users.get(i).getAvatarUrl());
                node.put("yjBalance", users.get(i).getYjBalance());
                node.put("isAdmin", users.get(i).getIsAdmin());
                node.put("loginphone", users.get(i).getLoginphone());
                node.put("invitecode", users.get(i).getInvitecode());
                node.put("myinvitecode", users.get(i).getMyinvitecode());
                node.put("myxfprice", users.get(i).getMyxfprice());
                node.put("xjxfprice", users.get(i).getXjxfprice());
                node.put("yjtxprice", users.get(i).getYjtxprice());
                node.put("remark", users.get(i).getRemark());


                node.put("list", getChildrenRecursively(users.get(i).getMyinvitecode()));
                log.info(users.get(i).getOpenid());
                result.add(node);
            }

        return result;
    }


    public String buildHierarchicalJson(List<Map<String, Object>> data) {
        JSONArray jsonArray = new JSONArray();

        for (Map<String, Object> node : data) {
            JSONObject jsonNode = new JSONObject();
            jsonNode.put("userid", node.get("userid"));
            jsonNode.put("openid", node.get("openid"));
            jsonNode.put("account", node.get("account"));
            jsonNode.put("totalxjprice",  node.get("totalxjprice"));
            jsonNode.put("realname", node.get("realname"));
            jsonNode.put("phone", node.get("phone"));
            jsonNode.put("twophone", node.get("twophone"));
            jsonNode.put("balance", node.get("balance"));
            jsonNode.put("isRead", node.get("isRead"));
            jsonNode.put("contact", node.get("contact"));
            jsonNode.put("nickName", node.get("nickName"));
            jsonNode.put("avatarUrl", node.get("avatarUrl"));
            jsonNode.put("yjBalance", node.get("yjBalance"));
            jsonNode.put("isAdmin", node.get("isAdmin"));
            jsonNode.put("loginphone", node.get("loginphone"));
            jsonNode.put("invitecode", node.get("invitecode"));
            jsonNode.put("myinvitecode", node.get("myinvitecode"));
            jsonNode.put("myxfprice", node.get("myxfprice"));
            jsonNode.put("xjxfprice", node.get("xjxfprice"));
            jsonNode.put("yjtxprice", node.get("yjtxprice"));
            jsonNode.put("remark", node.get("remark"));
            if (node.containsKey("list") && node.get("list")!=null) {
                jsonNode.put("list", buildHierarchicalJson((List<Map<String, Object>>) node.get("list")));
            }
            jsonArray.add(jsonNode);
        }

        return jsonArray.toString();
    }

    @CrossOrigin
    @PostMapping("excel")
    @ApiOperation("下级总消费导出")
    @ApiImplicitParams({
    })
    public void export(HttpServletResponse response, @RequestBody JSONObject jsonData) throws IOException {
        //最终转出json
        List<Xjxf> list = new ArrayList<>();
        JSONArray jsonArray  = jsonData.getJSONArray("list");
        list = JSONObject.parseArray(jsonArray.toString(),Xjxf.class);
        ExcelUtil<Xjxf> util = new ExcelUtil<>(Xjxf.class);
        util.exportExcel(response, list, "下级总消费导出");

    }


    @PostMapping("/updatatxprice")
    @ApiOperation("根据用户id更新已经体现金额（传金额  自动累加的）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
            @ApiImplicitParam(value = "提现金额", name = "yjtxprice",paramType = "query"),
    })
    public AjaxResult updatatxprice(@RequestBody User user) {

        User user1 = userService.selectByid(user.getUserid());
        BigDecimal yjtxprice = user1.getYjtxprice();
        yjtxprice = yjtxprice.add(user.getYjtxprice());

        return AjaxResult.success(userService.yjtxprice(yjtxprice,user.getUserid()));
    }



    @PostMapping("/updatainvitecode")
    @ApiOperation("根据用户id绑定用户邀请码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
            @ApiImplicitParam(value = "上级邀请码", name = "invitecode",paramType = "query"),
    })
    public AjaxResult updatainvitecode(@RequestBody User user) {
        return AjaxResult.success(userService.updatainvitecode(user.getInvitecode(),user.getUserid()));
    }


    @PostMapping("/updatazl")
    @ApiOperation("根据用户id修改个人资料")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone",paramType = "query"),
            @ApiImplicitParam(value = "真实姓名", name = "realname",paramType = "query"),
            @ApiImplicitParam(value = "第二手机号", name = "twophone",paramType = "query"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
            @ApiImplicitParam(value = "昵称", name = "nickName",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "头像url地址", name = "avatarUrl",paramType = "query",dataType = "string"),
    })
    public AjaxResult updatazl(@RequestBody User user) {
        return userService.updataUserzl(user.getPhone(),user.getRealname(),user.getTwophone(),user.getNickName(),user.getAvatarUrl(),user.getUserid());

    }


    @PostMapping("/updataAdmin")
    @ApiOperation("根据用户id是否为管理员  1管理员  0 不是管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "管理员状态 1管理员  0 不是管理员", name = "isAdmin",paramType = "query",dataType = "int"),
    })
    public AjaxResult updataAdmin(@RequestHeader("token") String token,@RequestBody User user) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return userService.updatAdmin(user.getIsAdmin(),user.getUserid());

    }



    @PostMapping("/updataRemark")
    @ApiOperation("根据用户id修改用户备注")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "用户备注", name = "remark",paramType = "query",dataType = "String"),
    })
    public AjaxResult updataRemark(@RequestHeader("token") String token,@RequestBody User user) {
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(userService.updatremark(user.getRemark(),user.getUserid()));

    }





    @PostMapping("/address")
    @ApiOperation("根据用户id新增地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
            @ApiImplicitParam(value = "收货地址", name = "address",paramType = "query"),
            @ApiImplicitParam(value = "收件人", name = "username",paramType = "query"),
            @ApiImplicitParam(value = "联系方式", name = "phone",paramType = "query"),
            @ApiImplicitParam(value = "身份证正面url", name = "idZ",paramType = "query"),
            @ApiImplicitParam(value = "身份证反面url", name = "idF",paramType = "query"),
            @ApiImplicitParam(value = "身份证号", name = "idNo",paramType = "query"),

    })
    public AjaxResult address(@RequestBody JSONObject jsonObject) throws Exception{
        Integer userid = jsonObject.getInteger("userid");
        String address = jsonObject.getString("address");
        String username = jsonObject.getString("username");
        String phone = jsonObject.getString("phone");
        String idZ = jsonObject.getString("idZ");
        String idF = jsonObject.getString("idF");
        String idNo = jsonObject.getString("idNo");
        return AjaxResult.success(userAddressService.Add(userid,address,username,phone,idZ,idF,idNo));
    }


    @PostMapping("/selectByAduserid")
    @ApiOperation("根据用户id查询地址信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
    })
    public AjaxResult selectByuserid(@RequestBody UserAddress userAddress) {
        return AjaxResult.success(userAddressService.selectByuserid(userAddress.getUserid()));
    }


    @PostMapping("/deleteByAdid")
    @ApiOperation("根据地址id删除地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "地址id", name = "id",paramType = "query"),
    })
    public AjaxResult deleteByid(@RequestBody UserAddress userAddress) {
        return AjaxResult.success(userAddressService.deleteByid(userAddress.getId()));
    }


    @PostMapping("/updataByAdid")
    @ApiOperation("根据地址id更新地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收货地址", name = "address",paramType = "query"),
            @ApiImplicitParam(value = "收件人", name = "username",paramType = "query"),
            @ApiImplicitParam(value = "联系方式", name = "phone",paramType = "query"),
            @ApiImplicitParam(value = "地址id", name = "id",paramType = "query"),
            @ApiImplicitParam(value = "身份证正面url", name = "idZ",paramType = "query"),
            @ApiImplicitParam(value = "身份证反面url", name = "idF",paramType = "query"),
            @ApiImplicitParam(value = "身份证号", name = "idNo",paramType = "query"),
    })
    public AjaxResult updataByid(@RequestBody JSONObject jsonObject) throws Exception{

        Integer id = jsonObject.getInteger("id");
        String address = jsonObject.getString("address");
        String username = jsonObject.getString("username");
        String phone = jsonObject.getString("phone");
        String idZ = jsonObject.getString("idZ");
        String idF = jsonObject.getString("idF");
        String idNo = jsonObject.getString("idNo");

        return AjaxResult.success(userAddressService.updataByid(address,username,phone,id,idZ,idF,idNo));
    }


    @PostMapping("/selectAdid")
    @ApiOperation("根据地址id查询地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "地址id", name = "id",paramType = "query"),
    })
    public AjaxResult selectid(@RequestBody UserAddress userAddress) {
        return AjaxResult.success(userAddressService.selectid(userAddress.getId()));
    }


    @PostMapping("/defaultType")
    @ApiOperation("根据地址id设为默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "地址id", name = "id",paramType = "query"),
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query"),
    })
    public AjaxResult defaultType(@RequestBody UserAddress userAddress) {
        if(userAddress.getId()==null){
            return AjaxResult.error("地址id不能为空");
        }
        if(userAddress.getUserid() ==null){
            return AjaxResult.error("用户id不能为空");
        }
        //先清空默认地址
        userAddressService.updataClear(userAddress.getUserid());
        return AjaxResult.success(userAddressService.defaultType(userAddress.getId(),userAddress.getUserid()));
    }



    @PostMapping("/selectAlluser")
    @ApiOperation("分页查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码，0表示第一页", name = "pageNumber",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "每页结果数", name = "pageSize",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "昵称", name = "nickName",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "手机号", name = "phone",paramType = "query",dataType = "string"),
    })
    public AjaxResult selectAll(@RequestHeader("token") String token,@RequestBody JSONObject jsonObject) {
        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");
        String  nickName = jsonObject.getString("nickName");
        String  phone = jsonObject.getString("phone");
        if(!redisUtils.hasKey(token)){
            return AjaxResult.noLoginError("登录失效");
        }
        return AjaxResult.success(userService.selectAll(pageNumber,pageSize,nickName,phone));
    }


    //更新用户昵称头像
/*    @PostMapping("/updataNickName")
    @ApiOperation("根据用户id修改用户头像昵称")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userid",paramType = "query",dataType = "int"),
            @ApiImplicitParam(value = "昵称", name = "nickName",paramType = "query",dataType = "string"),
            @ApiImplicitParam(value = "头像url地址", name = "avatarUrl",paramType = "query",dataType = "string"),
    })
    public AjaxResult updataNickName(@RequestBody JSONObject jsonObject) {

        String nickName = jsonObject.getString("nickName");
        String avatarUrl = jsonObject.getString("avatarUrl");
        Integer userid = jsonObject.getInteger("userid");
        return AjaxResult.success(userService.updatanickName(nickName,avatarUrl,userid));
    }*/


        private SessionOutVo getSessionId(Integer userId) {
        long currentTimeMillis = System.currentTimeMillis();
        int redisExpireMinite = SsoLoginStore.getRedisExpireMinite();
        long expireFreshTime = currentTimeMillis + redisExpireMinite * 60 * 1000 / 2;
        // 1、make xxl-sso user
        XxlSsoUser xxlUser = new XxlSsoUser();
        xxlUser.setUserid(String.valueOf(userId));
        xxlUser.setUsername(String.valueOf(userId));
        xxlUser.setVersion(UUIDUtils.getUUID());
        xxlUser.setExpireMinite(redisExpireMinite);
        xxlUser.setExpireFreshTime(expireFreshTime);
        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);
        // 3、login, store storeKey
        SsoTokenLoginHelper.login(sessionId, xxlUser);
        // 4、数据返回
        SessionOutVo session = new SessionOutVo();
        session.setCurrentTimeMillis(currentTimeMillis);
        session.setExpireMinute(redisExpireMinite);
        session.setSessionId(sessionId);
        session.setExpireFreshTime(expireFreshTime);
        return session;
    }

}
