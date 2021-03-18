package org.clazh.controller;

import lombok.extern.log4j.Log4j;
import org.clazh.dto.BoardDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@Log4j
public class BoardController {

    @GetMapping("/register")
    public void registerGet() {

    }

    @PostMapping("/register")
    @ResponseBody
    public String registerPost(@RequestBody BoardDTO dto) {

        log.info("post...................");
        log.info(dto);

        return "success";
    }

}
