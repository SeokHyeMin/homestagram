package com.home.inmy.like;

import com.home.inmy.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    List<Likes> findByAccount(Account account);

    Page<Likes> findByAccount(Account account, Pageable pageable);

    Likes findByPostAndAccount(Post post, Account account);
}
