package com.home.inmy.follow;

import com.home.inmy.domain.Account;

public interface FollowService {

    void save(String toLoginId, Account fromAccount);

    boolean findFollow(String toLoginId, Account fromAccount);

    void unfollow(String loginId, Account account);
}
