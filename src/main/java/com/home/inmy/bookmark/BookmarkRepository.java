package com.home.inmy.bookmark;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Bookmark;
import com.home.inmy.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByAccount(Account account);

    Page<Bookmark> findByAccount(Account account, Pageable pageable);

    Bookmark findByPostAndAccount(Post post, Account account);
}
