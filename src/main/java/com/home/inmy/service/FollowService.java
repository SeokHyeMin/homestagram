package com.home.inmy.follow;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Follow;

import java.util.List;

public interface FollowService {

    void save(String toLoginId, Account fromAccount);

    boolean findFollow(String toLoginId, Account fromAccount);

    void unfollow(String loginId, Account account);

    List<Follow> getFollowList(Account account);

    List<Follow> getFollowerList(Account account);
}
