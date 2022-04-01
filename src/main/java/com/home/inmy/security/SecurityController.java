package com.home.inmy.security;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/error/denied")
    public String deniedPage(@CurrentUser Account account, Model model){
        model.addAttribute("account",account);
        return "/error/denied";
    }
}
