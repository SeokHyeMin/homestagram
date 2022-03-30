package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Bookmark;
import com.home.inmy.domain.entity.Post;

import java.util.List;

public interface BookmarkService {

    void addBookmark(Post post, Account account);

    void removeBookmark(Post post, Account account);

    void deletePostBookmark(Post post);

    boolean accountPostBookmark(Post post, Account account);

    List<Long> getLikePostNum(List<Bookmark> account);

    List<Bookmark> getBookmarkList(Account account);
}
