package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Likes;
import com.home.inmy.domain.entity.Post;

import java.util.List;

public interface LikeService {

    void addLike(Post post, Account account);

    void removeLike(Post post, Account account);

    void deletePostLike(Post post);

    boolean accountPostLike(Post post, Account account);

    List<Long> getLikePostNum(List<Likes> account);

    List<Likes> getLikeList(Account account);
}
