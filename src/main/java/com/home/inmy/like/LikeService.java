package com.home.inmy.like;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;

public interface LikeService {

    void addLike(Post post, Account account);

    void removeLike(Post post, Account account);

}
