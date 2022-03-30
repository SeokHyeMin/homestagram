package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Follow;

import java.util.List;

public interface FollowService {

    void save(String toLoginId, Account fromAccount);

    boolean findFollow(String toLoginId, Account fromAccount);

    void unfollow(String loginId, Account account);

    List<Follow> getFollowList(Account account);

    List<Follow> getFollowerList(Account account);
}
