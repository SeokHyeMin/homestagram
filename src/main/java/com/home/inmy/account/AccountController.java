package com.home.inmy.account;

import com.home.inmy.account.form.SignUpForm;
import com.home.inmy.account.validator.SignUpFormValidator;
import com.home.inmy.domain.Follow;
import com.home.inmy.domain.Likes;
import com.home.inmy.domain.Post;
import com.home.inmy.domain.Account;
import com.home.inmy.follow.FollowServiceImpl;
import com.home.inmy.like.LikeServiceImpl;
import com.home.inmy.post.PostRepository;
import com.home.inmy.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
        Boolean follow = followService.findFollow(loginId, account); //로그인 계정, 프로필 주인 계정
        List<Follow> following = followService.getFollowList(accountByLoginId); //팔로잉 리스트
        List<Follow> follower = followService.getFollowerList(accountByLoginId); //팔로잉 리스트


        log.info(String.valueOf(follow));

        model.addAttribute("isOwner", accountByLoginId.getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("account",accountByLoginId);
        model.addAttribute("follow",follow);
        model.addAttribute("following",following);
        model.addAttribute("follower",follower);
        model.addAttribute(postList);
        model.addAttribute("listText","사진");

        return "account/profile";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{listType}/{loginId}")
    public String profileLike(@PathVariable String loginId, @PathVariable String listType, Model model, @CurrentUser Account account) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인
        List<Post> postList = new ArrayList<>();
        String listText = null;

        if(listType.equals("like")){
            List<Likes> postLists = likeService.getLikeList(accountByLoginId);
            for (Likes list : postLists) {
                postList.add(list.getPost());
            }
            listText = "좋아요";
        }else if(listType.equals("photoList")){
            postList = postRepository.findByAccount(account);
            listText = "사진";
        }

        model.addAttribute("postList", postList);
        model.addAttribute("listText",listText);

        return "account/profile :: #profile-postList";
    }

}
