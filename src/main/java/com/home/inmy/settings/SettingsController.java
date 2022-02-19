package com.home.inmy.settings;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.account.form.ProfileForm;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final ModelMapper modelMapper;

    @GetMapping("/settings/profile")
    public String profile(@CurrentUser Account account, Model model){

        model.addAttribute(account);
        model.addAttribute(new ProfileForm());

        return "settings/profile";
    }

}
