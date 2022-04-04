package com.home.inmy.service.impl;

import com.home.inmy.repository.AccountRepository;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Follow;
import com.home.inmy.repository.FollowRepository;
import com.home.inmy.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final AccountRepository accountRepository;

    @Override
    public void save(String toLoginId, Account fromAccount) {

        Account toAccount = accountRepository.findByLoginId(toLoginId);

        Follow newFollow = Follow.builder()
                            .fromAccount(fromAccount)
                            .toAccount(toAccount)
                            .build();

        followRepository.save(newFollow);
    }

    @Override
    public boolean findFollow(String toLoginId, Account fromAccount) {

        Account toAccount = accountRepository.findByLoginId(toLoginId);

        Follow follow = followRepository.findByFromAccountAndToAccount(fromAccount, toAccount);

        return follow != null; //follow 하고 있다면 true, 안하면 false
    }

    @Override
    public void unfollow(String toLoginId, Account fromAccount) {

        Account toAccount = accountRepository.findByLoginId(toLoginId);

        followRepository.deleteByFromAccountAndToAccount(fromAccount, toAccount);
    }

    @Override
    public List<Follow> getFollowerList(Account account) { //팔로워 리스트

        return followRepository.findAllByToAccount(account);
    }

    @Override
    public Page<Follow> getFollowerList(Account account, int page) { //팔로워 리스트
        PageRequest pageRequest = PageRequest.of(page, 4);
        return followRepository.findAllByToAccount(account,pageRequest);
    }

    @Override
    public Page<Follow> getFollowList(Account account, int page) { //팔로잉 리스트
        PageRequest pageRequest = PageRequest.of(page, 6);
        return followRepository.findAllByFromAccount(account, pageRequest);
    }
}
