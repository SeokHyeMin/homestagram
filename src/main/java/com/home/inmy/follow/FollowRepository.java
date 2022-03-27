package com.home.inmy.follow;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @EntityGraph(value = "Follow.withAll")
    Follow findByFromAccountAndToAccount(Account fromAccount, Account toAccount);

    @EntityGraph(value = "Follow.withAll")
    void deleteByFromAccountAndToAccount(Account fromAccount, Account toAccount);

    @EntityGraph(value = "Follow.withAll")
    List<Follow> findAllByToAccount(Account account); //팔로잉 리스트

    @EntityGraph(value = "Follow.withAll")
    List<Follow> findAllByFromAccount(Account account); //팔로워 리스트

    @EntityGraph(value = "Follow.withAll")
    Page<Follow> findAllByToAccount(Account account, Pageable pageable); //팔로잉 리스트

    @EntityGraph(value = "Follow.withAll")
    Page<Follow> findAllByFromAccount(Account account, Pageable pageable); //팔로워 리스트
}
