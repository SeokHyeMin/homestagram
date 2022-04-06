package com.home.inmy.controller;

import com.home.inmy.service.AccountService;
import com.home.inmy.service.FollowService;
import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Follow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor @Slf4j
public class FollowController {

    private final FollowService followService;
    private final AccountService accountService;

    //프로필페이지에서 팔로우 버튼 눌렀을 경우 로직
    @GetMapping("/profile/follow/{ownerLoginId}")
    @ResponseBody
    public Integer profileFollow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        followService.save(ownerLoginId, account);
        Account profileOwner = accountService.getAccount(ownerLoginId);

        return followService.getFollowerList(profileOwner).size(); //화면의 팔로워 숫자를 바꿔주기 위해 반환.
    }

    //프로필 페이지에서 언팔로우 했을 경우
    @GetMapping("/profile/unfollow/{ownerLoginId}")
    @ResponseBody
    public Integer profileUnfollow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        followService.unfollow(ownerLoginId, account);
        Account profileOwner = accountService.getAccount(ownerLoginId);

        return followService.getFollowerList(profileOwner).size(); //화면의 팔로워 숫자를 바꿔주기 위해 반환.
    }

    //팔로우 버튼 눌렀을 때 (게시글에서)
    @GetMapping("/follow/{ownerLoginId}")
    public void follow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        if(account != null){
            followService.save(ownerLoginId, account);
        }
    }

    //언팔로우 버튼 눌렀을 때 (게시글에서)
    @GetMapping("/unfollow/{ownerLoginId}")
    public void unfollow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        followService.unfollow(ownerLoginId, account);

    }

    //팔로잉 리스트 보여주기
    @GetMapping("/followList/{ownerLoginId}")
    public String followList(@PathVariable String ownerLoginId, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Account account = accountService.getAccount(ownerLoginId);

        Page<Follow> followList = followService.getFollowList(account,page); //프로필 주인 팔로우 하는 사람들

        int pageNum = followList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 3; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = followList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("followList", followList);
        model.addAttribute("ownerLoginId",ownerLoginId);

       return "fragments :: #followList";
    }

    //팔로워 리스트 보여주기
    @GetMapping("/followerList/{ownerLoginId}")
    public String followerList(@PathVariable String ownerLoginId, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Account account = accountService.getAccount(ownerLoginId);

        Page<Follow> followerList = followService.getFollowerList(account,page); //프로필 주인 팔로워들
        log.info(String.valueOf(page));
        log.info("확인");
        int pageNum = followerList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 3; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = followerList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("followerList", followerList);
        model.addAttribute("ownerLoginId",ownerLoginId);

        return "fragments :: #followerList";
    }
}
