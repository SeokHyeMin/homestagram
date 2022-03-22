package com.home.inmy.account;

import com.home.inmy.account.form.SignUpForm;
import com.home.inmy.account.validator.SignUpFormValidator;
import com.home.inmy.bookmark.BookmarkServiceImpl;
import com.home.inmy.domain.*;
import com.home.inmy.follow.FollowServiceImpl;
import com.home.inmy.like.LikeServiceImpl;
import com.home.inmy.post.PostRepository;
import com.home.inmy.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final BookmarkServiceImpl bookmarkService;


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
    public String profile(@PathVariable String loginId, Model model, @CurrentUser Account account, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인

        Page<Post> postList = postService.profilePageList(accountByLoginId,page);
        int totalPage = postList.getTotalPages();
        Boolean follow = followService.findFollow(loginId, account); //로그인 계정, 프로필 주인 계정
        List<Follow> following = followService.getFollowList(accountByLoginId); //팔로잉 리스트
        List<Follow> follower = followService.getFollowerList(accountByLoginId); //팔로잉 리스트

        model.addAttribute("isOwner", accountByLoginId.getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("account",accountByLoginId); //프로필 주인
        model.addAttribute("follow",follow); //해당계정을 팔로잉하는지 안하는지
        model.addAttribute("following",following);
        model.addAttribute("follower",follower);
        model.addAttribute("postList",postList);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("listText","게시물");

        return "account/profile";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{listType}/{loginId}")
    public String profileLike(@PathVariable String loginId, @PathVariable String listType, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인
        String listText = null;
        String returnPage = null;
        int totalPage = 0;

        switch (listType) { //sub-nav 누른 것에 따라 보여지는 list 변경.
            case "like":
                Page<Likes> likesList = likeService.getProfileLikeList(accountByLoginId, page);
                totalPage = likesList.getTotalPages();
                listText = "좋아요";
                returnPage = "fragments :: #profile-Lists";
                model.addAttribute("postList", likesList);
                break;
            case "photoList":
                Page<Post> postList = postService.profilePageList(accountByLoginId, page);
                totalPage = postList.getTotalPages();
                listText = "게시물";
                returnPage = "account/profile :: #profile-postList";
                model.addAttribute("postList", postList);
                break;
            case "bookmarkList":
                Page<Bookmark> bookmarkList = bookmarkService.getProfileBookmarkList(accountByLoginId, page);
                totalPage = bookmarkList.getTotalPages();
                listText = "북마크";
                returnPage = "fragments :: #profile-Lists";
                model.addAttribute("postList", bookmarkList);
                break;
        }

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("listText",listText);

        return returnPage;
    }

}
