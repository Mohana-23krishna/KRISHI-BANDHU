package com.buildathon.krishibandhu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/buyer")
    public String buyer() {
        return "buyer";
    }
    
    @GetMapping("/farmer-register")
    public String farmerRegister() {
        return "farmer-register";
    }
    
    @GetMapping("/voice-test")
    public String voiceTest() {
        return "voice-test";
    }
}

