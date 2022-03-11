package com.home.inmy.like;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Likes;
import com.home.inmy.domain.Post;

import java.util.List;

public interface LikeService {

    void addLike(Post post, Account account);

    void removeLike(Post post, Account account);

    void deletePostLike(Post post);

    boolean accountPostLike(Post post, Account account);

    List<Long> getLikePostNum(List<Likes> account);

    List<Likes> getLikeList(Account account);
}
