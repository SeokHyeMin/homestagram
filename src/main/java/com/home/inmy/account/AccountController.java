package com.home.inmy.account;

import com.home.inmy.account.form.SignUpForm;
import com.home.inmy.account.validator.SignUpFormValidator;
import com.home.inmy.post.Post;
import com.home.inmy.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;
    private final PostRepository postRepository;
    private final EntityManager em;

    @InitBinder("signUpForm") //
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors) {

        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{loginId}")
    public String profile(@PathVariable String loginId, Model model, @CurrentUser Account account) {

        Account accountByLoginId = accountRepository.findByLoginId(loginId); //해당 프로필 주인
        if (accountByLoginId == null) {
            throw new IllegalArgumentException(loginId + "에 해당하는 사용자가 없습니다.");
        }


            String jpql = "select distinct p from Post p join fetch p.imageFiles where p.writer = '" + loginId + "'";
            List<Post> postList = em.createQuery(jpql, Post.class).getResultList();

            model.addAttribute("isOwner", accountByLoginId.getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
            model.addAttribute(accountByLoginId);
            model.addAttribute(postList);
            model.addAttribute("count", postList.size());
            log.info(String.valueOf(postList.size()));

            return "account/profile";
    }

}
