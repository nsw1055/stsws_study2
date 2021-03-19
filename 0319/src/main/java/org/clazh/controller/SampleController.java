package org.clazh.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
@Log4j
public class SampleController {

    @GetMapping("/all")
    public void all() {

    }

    @GetMapping("/member")
    public void member() {

    }

    @GetMapping("/admin")
    public void admin() {

    }
}
