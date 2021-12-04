package com.wx.nuo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @RequestMapping
    public String helloWord() {
        return "Welcome to the world of spring boot.";
    }
}
