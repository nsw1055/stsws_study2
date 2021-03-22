package org.clazh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public class TestController {

    @GetMapping("/index")
    public void test() {

    }
}
