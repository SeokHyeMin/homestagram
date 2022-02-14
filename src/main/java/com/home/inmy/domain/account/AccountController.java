package com.home.inmy.domain.account;

import com.home.inmy.domain.account.form.SignUpForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {




    @GetMapping("/sign-up")
    public String singUpForm(Model model){
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

}
