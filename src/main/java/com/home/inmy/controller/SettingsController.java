package com.home.inmy.controller;

import com.home.inmy.form.AccountForm;
import com.home.inmy.form.PasswordForm;
import com.home.inmy.form.ProfileForm;
import com.home.inmy.service.impl.AccountServiceImpl;
import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.validator.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final AccountServiceImpl accountService;
    private final AccountFormValidator accountFormValidator;
    private final ModelMapper modelMapper;


    @GetMapping("/settings/profile")
    public String profileView(@CurrentUser Account account, Model model){

        model.addAttribute(account);
        model.addAttribute(new ProfileForm(account));

        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String updateProfileForm(@CurrentUser Account account, @Valid ProfileForm profileForm, Errors errors, Model model,
                                    RedirectAttributes attributes){

        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/profile";
        }

        accountService.updateProfile(account, profileForm);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");

        return "redirect:/" + "settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordView(@CurrentUser Account account, Model model){

        model.addAttribute(account);
        model.addAttribute(new PasswordForm());

        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
                                 Model model, RedirectAttributes attributes){

        if(errors.hasErrors()){
          model.addAttribute(account);
          return "settings/password";
        }

        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.validate(passwordForm, errors);
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/password";
        }

        String newPassword = passwordForm.getNewPassword();
        accountService.updatePassword(account, newPassword);
        attributes.addFlashAttribute("message","비밀번호를 변경하였습니다.");

        return "redirect:/" + "settings/password";
    }

    @GetMapping("/settings/account")
    public String accountView(@CurrentUser Account account, Model model){

        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, AccountForm.class));

        return "settings/account";
    }

    @PostMapping("/settings/account")
    public String updateAccount(@CurrentUser Account account, @Valid AccountForm accountForm, Errors errors, Model model,
                                RedirectAttributes attributes){

        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/account";
        }

        accountFormValidator.check(account, accountForm);
        accountFormValidator.validate(accountForm, errors);

        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/account";
        }

        accountService.updateAccount(account, accountForm);
        accountFormValidator.checkInitialization();
        attributes.addFlashAttribute("message","계정정보를 수정하였습니다.");

        return "redirect:/" + "settings/account";
    }

}
