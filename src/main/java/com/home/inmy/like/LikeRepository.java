package com.home.inmy.like;

import com.home.inmy.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    List<Likes> findByAccount(Account account);

    Likes findByPostAndAccount(Post post, Account account);
}
