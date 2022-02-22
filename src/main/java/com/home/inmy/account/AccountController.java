package com.home.inmy.account;

import com.home.inmy.account.form.SignUpForm;
import com.home.inmy.account.validator.SignUpFormValidator;
import com.home.inmy.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm") //
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors){

        if(errors.hasErrors()){
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";
    }

    @PostMapping ("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/profile/{loginId}")
    public String profile(@PathVariable String loginId, Model model, @CurrentUser Account account){

        Account accountByLoginId = accountRepository.findByLoginId(loginId); //해당 프로필 주인
        if(accountByLoginId == null){
            throw new IllegalArgumentException(loginId + "에 해당하는 사용자가 없습니다.");
        }
        model.addAttribute("isOwner", accountByLoginId.equals(account)); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute(accountByLoginId);

        return "account/profile";
    }

    @GetMapping("/profile")
    public String profile(Model model, @CurrentUser Account account){

        //내 정보를 통해 이동.

        model.addAttribute("isOwner", true); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute(account);

        return "account/profile";
    }

}
