package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Follow;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FollowService {

    void save(String toLoginId, Account fromAccount);

    boolean findFollow(String toLoginId, Account fromAccount);

    void unfollow(String loginId, Account account);

    List<Follow> getFollowerList(Account account);

    Page<Follow> getFollowerList(Account account, int page);

    Page<Follow> getFollowList(Account account, int page);
}
