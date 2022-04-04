package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Likes;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LikeService {

    void addLike(Post post, Account account);

    void removeLike(Post post, Account account);

    boolean accountPostLike(Post post, Account account);

    List<Likes> getLikeList(Account account);

    Page<Likes> getProfileLikeList(Account account, int page);
}
