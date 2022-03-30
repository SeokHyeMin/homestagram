package com.home.inmy.repository;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Bookmark;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByAccount(Account account);

    Page<Bookmark> findByAccount(Account account, Pageable pageable);

    Bookmark findByPostAndAccount(Post post, Account account);
}
