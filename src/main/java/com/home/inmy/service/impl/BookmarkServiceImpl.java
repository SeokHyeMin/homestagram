package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Bookmark;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.repository.BookmarkRepository;
import com.home.inmy.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Override
    public void addBookmark(Post post, Account account) {

        Bookmark bookmark = Bookmark.builder()
                .account(account)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Override
    public void removeBookmark(Post post, Account account) {

        Bookmark bookmark = bookmarkRepository.findByPostAndAccount(post, account);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public boolean accountPostBookmark(Post post, Account account) {

        Bookmark bookmark = bookmarkRepository.findByPostAndAccount(post, account);
        //현재 게시글을 북마크에 추가하였다면 true, 아니면 false 반환.
        return bookmark != null;
    }

    @Override
    public List<Bookmark> getBookmarkList(Account account) {
        return bookmarkRepository.findByAccount(account);
    }

    @Override
    public Page<Bookmark> getProfileBookmarkList(Account account, int page) {

        PageRequest pageRequest = PageRequest.of(page, 8);
        return bookmarkRepository.findByAccount(account, pageRequest);
    }
}
