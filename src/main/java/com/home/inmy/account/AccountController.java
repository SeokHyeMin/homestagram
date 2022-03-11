package com.home.inmy.account;

import com.home.inmy.account.form.SignUpForm;
import com.home.inmy.account.validator.SignUpFormValidator;
import com.home.inmy.domain.Likes;
import com.home.inmy.domain.Post;
import com.home.inmy.domain.Account;
import com.home.inmy.follow.FollowServiceImpl;
import com.home.inmy.like.LikeServiceImpl;
import com.home.inmy.post.PostRepository;
import com.home.inmy.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final PostRepository postRepository;

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    private final LikeServiceImpl likeService;
    private final FollowServiceImpl followService;
    private final PostServiceImpl postService;


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

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인

        List<Post> postList = postRepository.findByAccount(account);

        model.addAttribute("isOwner", accountByLoginId.getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute(accountByLoginId);
        model.addAttribute(postList);
        model.addAttribute("count", postList.size());
        log.info(String.valueOf(postList.size()));

        return "account/profile";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/like/{loginId}") //해당프로필에서 프로필 주인이 좋아요한 게시물만 보기.
    public String profileLike(@PathVariable String loginId, Model model, @CurrentUser Account account) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인

        List<Likes> postList = likeService.getLikeList(accountByLoginId);
        Boolean follow = followService.findFollow(account.getLoginId(), accountByLoginId); //로그인 계정, 프로필 주인 계정

        model.addAttribute("isOwner", accountByLoginId.getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("follow",follow);
        model.addAttribute("ownerLoginId",accountByLoginId.getLoginId());
        model.addAttribute(postList);
        model.addAttribute("count", postList.size());

        return "account/profile";
    }
}
