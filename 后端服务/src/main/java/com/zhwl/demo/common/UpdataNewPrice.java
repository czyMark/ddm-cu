package com.zhwl.demo.common;

import com.alibaba.fastjson.JSONObject;
import com.zhwl.demo.po.GoodOrder;
import com.zhwl.demo.service.GetHtml;
import com.zhwl.demo.service.GoodOrderService;
import com.zhwl.demo.service.GoodService;
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
public class UpdataNewPrice {


    @Autowired
    private GoodOrderService goodOrderService;

    @Autowired
    private GetHtml getHtml;

    @Scheduled(fixedDelay = 150000) // 每天0点0分执行
    public void clearClockinStatus() throws Exception {

        Byte type = 10;
       //查询所有被超越的订单  和  竞拍失败的订单   更新最新价格
        List<GoodOrder> goodOrderList =  goodOrderService.selectByType(type);

        for (int o=0;o<goodOrderList.size();o++){
            String goodcode = goodOrderList.get(o).getGoodCode();
            String lowHtml = getHtml.notice("https://page.auctions.yahoo.co.jp/jp/auction/"+goodcode,"");
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
            BigDecimal jpprice = detail.getBigDecimal("price");//现在价格;  //现价
            //付新的值
            goodOrderService.updataNowPrice(jpprice,goodOrderList.get(o).getId());
        }
    }

}
