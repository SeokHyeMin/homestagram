package com.home.inmy.controller;

import com.home.inmy.service.impl.AccountServiceImpl;
import com.home.inmy.domain.CurrentUser;
import com.home.inmy.form.SignUpForm;
import com.home.inmy.validator.SignUpFormValidator;
import com.home.inmy.service.impl.BookmarkServiceImpl;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Bookmark;
import com.home.inmy.domain.entity.Likes;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.service.impl.FollowServiceImpl;
import com.home.inmy.service.impl.LikeServiceImpl;
import com.home.inmy.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImpl accountService;
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

        accountService.createAccount(signUpForm);

        return "/login";
    }

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@CurrentUser Account account, @RequestParam String profileOwnerLoginId, HttpSession session){

        if(profileOwnerLoginId.equals(account.getLoginId())){
            accountService.deleteAccount(account.getId());
            session.invalidate();
        }else{
            throw new IllegalArgumentException("계정을 삭제할 권한이 없습니다.");
        }

        return "redirect:/";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{loginId}")
    public String profile(@PathVariable String loginId, Model model, @CurrentUser Account account, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인

        Page<Post> postList = postService.profilePageList(accountByLoginId,page);
        Boolean follow = followService.findFollow(loginId, account); //로그인 계정, 프로필 주인 계정
        Boolean isOwner = account != null && accountByLoginId.getLoginId().equals(account.getLoginId()); //현재 계정이 게시글 주인인지 판단.

        int pageNum = postList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = postList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("isOwner",isOwner);
        model.addAttribute("owner",accountByLoginId); //프로필 주인 계정
        model.addAttribute("follow",follow); //해당계정을 팔로잉하는지 안하는지
        model.addAttribute("postList",postList);
        model.addAttribute("listText","게시물");
        model.addAttribute("account",account); //현재 로그인 계정

        return "account/profile";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{listType}/{loginId}")
    public String profileLike(@PathVariable String loginId, @PathVariable String listType, Model model,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인
        String listText = null;
        String returnPage = null;
        int totalPage = 0;
        int pageNum = 0;

        switch (listType) { //sub-nav 누른 것에 따라 보여지는 list 변경.
            case "like":
                Page<Likes> likesList = likeService.getProfileLikeList(accountByLoginId, page);
                pageNum = likesList.getPageable().getPageNumber(); // 현재 페이지
                totalPage = likesList.getTotalPages();
                listText = "좋아요";
                returnPage = "fragments :: #profile-Lists";
                model.addAttribute("postList", likesList);
                break;
            case "photoList":
                Page<Post> postList = postService.profilePageList(accountByLoginId, page);
                pageNum = postList.getPageable().getPageNumber(); // 현재 페이지
                totalPage = postList.getTotalPages();
                listText = "게시물";
                returnPage = "account/profile :: #profile-postList";
                model.addAttribute("postList", postList);
                break;
            case "bookmarkList":
                Page<Bookmark> bookmarkList = bookmarkService.getProfileBookmarkList(accountByLoginId, page);
                pageNum = bookmarkList.getPageable().getPageNumber(); // 현재 페이지
                totalPage = bookmarkList.getTotalPages();
                listText = "북마크";
                returnPage = "fragments :: #profile-Lists";
                model.addAttribute("postList", bookmarkList);
                break;
        }

        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("listText",listText);

        return returnPage;
    }

    @GetMapping("/find-password")
    public String findPasswordView(Model model, @CurrentUser Account account){

        log.info("--------find-password-------");
        model.addAttribute("account",account);

        return "account/findPw";
    }

    @PostMapping("/find-password")
    public String findPassword(@CurrentUser Account account, Model model, @RequestParam String loginId){

        Account findAccount = accountService.getAccount(loginId);

        if(findAccount == null){
            model.addAttribute("message","해당 아이디는 존재하지 않는 아이디입니다.");
            return "account/findPw";
        }

        accountService.findPassword(findAccount); //임시비밀번호 발급후 메일 보내주기.

        return "/login";
    }

}
