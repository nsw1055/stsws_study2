package org.clazh.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j
public class CommonController {

    @GetMapping("/accessError")
    public void accessDenied(Authentication auth, Model model){
        log.info("-------------------");
        log.info(auth);
        model.addAttribute("msg", "권한이 없는 사용자 입니다.");
    }

    @GetMapping("/customLogin")
    public void loginPage(String error, String logout, Model model) {
        log.info("error: "+error);
        log.info("logout: "+ logout);

        //로그아웃
        if(logout != null){
            model.addAttribute("logout", "Logout!!!!");
        }
    }
    @GetMapping("/customLogout")
    public void customLogout(){

    }
}
