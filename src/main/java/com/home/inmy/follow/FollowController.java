package com.home.inmy.follow;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowServiceImpl followService;

    @PostMapping("/follow/{loginId}")
    public Boolean follow(@CurrentUser Account account, @PathVariable String loginId){

        if(account == null){
            //TODO 로그인 페이지 혹은 로그인 경고창 띄우기
            return null;
        }

        followService.save(loginId, account);

        return true;
    }

    @PostMapping("/unfollow/{loginId}")
    public Boolean unfollow(@CurrentUser Account account, @PathVariable String loginId){

        followService.unfollow(loginId, account);

        return false;
    }
}
