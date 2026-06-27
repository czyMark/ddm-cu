package com.zhwl.demo.common;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.mapper.GoodOrderMapper;
import com.zhwl.demo.po.GoodOrder;
import com.zhwl.demo.service.CheackWon;
import com.zhwl.demo.service.YuyueService;
import com.zhwl.demo.service.impl.GetHtmlImpl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class Deleteshibai {

    @Autowired
    private GoodOrderMapper goodOrderMapper;

    @Autowired
    private GetHtmlImpl getHtml;

    @Autowired
    private CheackWon cheackWon;

    @Autowired
    private YuyueService yuyueService;

    @Scheduled(fixedDelay = 300000) // 每天0点0分执行
    public void clearClockinStatus() throws Exception {

        //商品竞拍结束删除所有竞拍失败的订单   和  竞拍被超越订单
        //编列所有竞拍失败的订单    若是商品一结束    立马删除
        List<GoodOrder> goodOrderList = goodOrderMapper.selectshibai();
        for (int k=0;k<goodOrderList.size();k++){
            try {
                cheackWon.cheack(goodOrderList.get(k).getGoodCode());
            }catch (Exception e){

            }
        }

        for (int i=0;i<goodOrderList.size();i++){
            GoodOrder goodOrder = goodOrderList.get(i);
            String code = goodOrder.getGoodCode();
            //查询是否失效    失效就删除
            String lowHtml = getHtml.notice("https://page.auctions.yahoo.co.jp/jp/auction/"+code,"");
            Document doc = Jsoup.parse(lowHtml);

            Elements jsonScripts = doc.select("script[type=application/json]");
            String data = jsonScripts.get(0).html();
            JSONObject jsonObject = JSONObject.parseObject(data);


            JSONObject props = jsonObject.getJSONObject("props");
            JSONObject pageProps = props.getJSONObject("pageProps");
            JSONObject initialState = pageProps.getJSONObject("initialState");
            JSONObject item = initialState.getJSONObject("item");
            JSONObject detail1 = item.getJSONObject("detail");
            JSONObject detail = detail1.getJSONObject("item");
            String status = detail.getString("status");
            if(status.equals("cancelled")||status.equals("closed")){
                goodOrderMapper.deleteByid(goodOrder.getId());
                log.info(goodOrder.getGoodCode()+"失效"+goodOrder.getUserid());
            }


            /*if(lowHtml.contains("ClosedHeader__tag")){
                //删除订单

            }*/

        }

        //编列所有竞拍被超越的订单    若是商品一结束    立马删除
        List<GoodOrder> cygoodlist = goodOrderMapper.selectchaoyue();
        for (int m=0;m<cygoodlist.size();m++){
             GoodOrder cygoodOrder = cygoodlist.get(m);
             String cycode = cygoodOrder.getGoodCode();
            //查询是否失效    失效就删除
            String lowHtml = getHtml.notice("https://page.auctions.yahoo.co.jp/jp/auction/"+cycode,"");
            Document doc = Jsoup.parse(lowHtml);
            Elements jsonScripts = doc.select("script[type=application/json]");
            String datas = jsonScripts.get(0).html();
            JSONObject jsonObject = JSONObject.parseObject(datas);


            JSONObject props = jsonObject.getJSONObject("props");
            JSONObject pageProps = props.getJSONObject("pageProps");
            JSONObject initialState = pageProps.getJSONObject("initialState");
            JSONObject item = initialState.getJSONObject("item");
            JSONObject detail1 = item.getJSONObject("detail");
            JSONObject detail = detail1.getJSONObject("item");
            String status = detail.getString("status");
            if(status.equals("cancelled")||status.equals("closed")){
                //删除订单
                goodOrderMapper.deleteByid(cygoodOrder.getId());
                log.info(cygoodOrder.getGoodCode()+"失效"+cygoodOrder.getUserid());
            }


        }


        List<GoodOrder> cygoodlistyy = goodOrderMapper.selectyuyue();
        for (int m=0;m<cygoodlistyy.size();m++){
            GoodOrder cygoodOrder = cygoodlistyy.get(m);
            String cycode = cygoodOrder.getGoodCode();
            //查询是否失效    失效就删除
            String lowHtml = getHtml.notice("https://page.auctions.yahoo.co.jp/jp/auction/"+cycode,"");
            Document doc = Jsoup.parse(lowHtml);
            Elements jsonScripts = doc.select("script[type=application/json]");
            String datas = jsonScripts.get(0).html();
            JSONObject jsonObject = JSONObject.parseObject(datas);


            JSONObject props = jsonObject.getJSONObject("props");
            JSONObject pageProps = props.getJSONObject("pageProps");
            JSONObject initialState = pageProps.getJSONObject("initialState");
            JSONObject item = initialState.getJSONObject("item");
            JSONObject detail1 = item.getJSONObject("detail");
            JSONObject detail = detail1.getJSONObject("item");
            String status = detail.getString("status");
            if(status.equals("cancelled")||status.equals("closed")){
                //删除订单
                goodOrderMapper.deleteByid(cygoodOrder.getId());
                log.info(cygoodOrder.getGoodCode()+"失效"+cygoodOrder.getUserid());
            }
        }


        //yuyueService.yuyue();

        log.info("失效订单删除成功");
    }


}
