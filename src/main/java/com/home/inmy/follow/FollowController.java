package com.home.inmy.follow;

import com.home.inmy.account.AccountService;
import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowServiceImpl followService;
    private final AccountService accountService;

    @GetMapping("/follow/{ownerLoginId}")
    public String follow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        if(account == null){
            //TODO 로그인 페이지 혹은 로그인 경고창 띄우기
            return null;
        }

        followService.save(ownerLoginId, account);

        return "redirect:/profile/{ownerLoginId}";
    }

    @GetMapping("/unfollow/{ownerLoginId}")
    public String unfollow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        followService.unfollow(ownerLoginId, account);

        return "redirect:/profile/{ownerLoginId}";
    }

    @GetMapping("/followList/{ownerLoginId}")
    public String followList(@PathVariable String ownerLoginId, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Account account = accountService.getAccount(ownerLoginId);

        Page<Follow> followList = followService.getFollowList(account,page); //프로필 주인 팔로우 하는 사람들
        int totalPage = followList.getTotalPages();
        model.addAttribute("followList", followList);
        model.addAttribute("totalPage",totalPage);

       return "fragments :: #followList";
    }

    @GetMapping("/followerList/{ownerLoginId}")
    public String followerList(@PathVariable String ownerLoginId, Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Account account = accountService.getAccount(ownerLoginId);

        Page<Follow> followerList = followService.getFollowerList(account,page); //프로필 주인 팔로워들
        int totalPage = followerList.getTotalPages();

        model.addAttribute("followerList", followerList);
        model.addAttribute("totalPage", totalPage);

        return "fragments :: #followerList";
    }
}
