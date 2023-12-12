package com.example.activiti8test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping
    public String login(){
        log.info("返回主页面");
        return "index.html";
    }
}
