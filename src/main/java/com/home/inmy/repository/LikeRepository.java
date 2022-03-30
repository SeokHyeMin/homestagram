package com.home.inmy.repository;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Likes;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    List<Likes> findByAccount(Account account);

    Page<Likes> findByAccount(Account account, Pageable pageable);

    Likes findByPostAndAccount(Post post, Account account);
}
