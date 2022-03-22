package com.home.inmy.follow;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFromAccountAndToAccount(Account fromAccount, Account toAccount);

    void deleteByFromAccountAndToAccount(Account fromAccount, Account toAccount);

    List<Follow> findAllByToAccount(Account account); //팔로잉 리스트

    List<Follow> findAllByFromAccount(Account account); //팔로워 리스트

    Page<Follow> findAllByToAccount(Account account, Pageable pageable); //팔로잉 리스트

    Page<Follow> findAllByFromAccount(Account account, Pageable pageable); //팔로워 리스트
}
