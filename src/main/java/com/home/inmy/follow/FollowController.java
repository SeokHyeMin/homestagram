package com.home.inmy.follow;

import com.home.inmy.account.AccountService;
import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowServiceImpl followService;
    private final AccountService accountService;

    @GetMapping("/profile/follow/{ownerLoginId}")
    @ResponseBody
    public Integer profileFollow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        followService.save(ownerLoginId, account);
        Account profileOwner = accountService.getAccount(ownerLoginId);

        return followService.getFollowerList(profileOwner).size(); //화면의 팔로워 숫자를 바꿔주기 위해 반환.
    }

    @GetMapping("/profile/unfollow/{ownerLoginId}")
    @ResponseBody
    public Integer profileUnfollow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        followService.unfollow(ownerLoginId, account);
        Account profileOwner = accountService.getAccount(ownerLoginId);

        return followService.getFollowerList(profileOwner).size(); //화면의 팔로워 숫자를 바꿔주기 위해 반환.
    }

    @GetMapping("/follow/{ownerLoginId}")
    @ResponseBody
    public ResponseEntity follow(@CurrentUser Account account, @PathVariable String ownerLoginId){

        if(account == null){
            return null;
        }
        followService.save(ownerLoginId, account);

        return ResponseEntity.ok().build();
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
