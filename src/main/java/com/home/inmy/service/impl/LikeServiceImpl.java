package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Likes;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.repository.LikeRepository;
import com.home.inmy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public void addLike(Post post, Account account) {
        Likes like = Likes.builder()
                .account(account)
                .post(post)
                .build();
        post.increaseLikes();
        likeRepository.save(like);
    }

    @Override
    public void removeLike(Post post, Account account) {
        Likes likes = likeRepository.findByPostAndAccount(post, account);
        post.decreaseLikes();
        likeRepository.delete(likes);
    }

    @Override
    public boolean accountPostLike(Post post , Account account){
        Likes likes = likeRepository.findByPostAndAccount(post, account);
        //현재 게시물을 좋아요 눌렀는지 아닌지 여부 판단 후 눌렀다면 true, 누르지 않았다면 false 반환
        return likes != null;
    }

    @Override
    public List<Likes> getLikeList(Account account) {
        return likeRepository.findByAccount(account);
    }

    @Override
    public Page<Likes> getProfileLikeList(Account account, int page) {
        PageRequest pageRequest = PageRequest.of(page, 8);
        return likeRepository.findByAccount(account, pageRequest);
    }

}
