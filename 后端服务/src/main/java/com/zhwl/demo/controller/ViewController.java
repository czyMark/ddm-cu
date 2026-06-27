package com.zhwl.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {


    /***
     * @desc 跳转到h5页面
     * @author yxs
     * @update yxs
     * @date 2021-11-06 15:22
     */
    @GetMapping("/")
    public String toH5(){
        return "h5/index";
    }


}
