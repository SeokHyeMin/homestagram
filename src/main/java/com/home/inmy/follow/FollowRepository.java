package com.home.inmy.follow;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFromAccountAndToAccount(Account fromAccount, Account toAccount);

    void deleteByFromAccountAndToAccount(Account fromAccount, Account toAccount);
}
