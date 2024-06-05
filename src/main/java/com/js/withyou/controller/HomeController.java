package com.js.withyou.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {


    /**
     * 홈페이지를 보여주는 API 입니다.
     * @return
     */
    @GetMapping("")
    public String showHomePage(){
        return "/home/home-mobile";
    }



}
