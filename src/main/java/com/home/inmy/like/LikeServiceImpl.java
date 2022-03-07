package com.home.inmy.like;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Likes;
import com.home.inmy.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;

    @Override
    public void addLike(Post post, Account account) {
        Likes like = Likes.builder()
                .account(account)
                .post(post)
                .build();

        likeRepository.save(like);
    }

    @Override
    public void removeLike(Post post, Account account) {
        Likes likes = likeRepository.findByPostAndAccount(post, account);
        likeRepository.delete(likes);
    }

}