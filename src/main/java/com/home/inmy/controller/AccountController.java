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

    @PostMapping("/delete-account")
    public void deleteAccount(@CurrentUser Account account, @RequestParam String profileOwnerLoginId){

        if(profileOwnerLoginId.equals(account.getLoginId())){
            accountService.deleteAccount(account.getId());
        }else{
            throw new IllegalArgumentException("계정을 삭제할 권한이 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{loginId}")
    public String profile(@PathVariable String loginId, Model model, @CurrentUser Account account, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Account accountByLoginId = accountService.getAccount(loginId); //해당 프로필 주인

        Page<Post> postList = postService.profilePageList(accountByLoginId,page);
        Boolean follow = followService.findFollow(loginId, account); //로그인 계정, 프로필 주인 계정

        int pageNum = postList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = postList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);

        model.addAttribute("owner",accountByLoginId); //프로필 주인 계정
        model.addAttribute("follow",follow); //해당계정을 팔로잉하는지 안하는지
        model.addAttribute("postList",postList);
        model.addAttribute("listText","게시물");
        model.addAttribute("account",account); //현재 로그인 계정

        return "account/profile";
    }

    @Transactional(readOnly = true)
    @GetMapping("/profile/{listType}/{loginId}")
    public String profileLike(@PathVariable String loginId, @PathVariable String listType, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

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

}