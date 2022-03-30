package com.home.inmy.repository;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>{

    @EntityGraph(value = "Post.withAccountAndImageFiles")
    List<Post> findTop4ByOrderByViewsDesc();

    Page<Post> findByAccount(Account account, Pageable pageable);

    @EntityGraph(value = "Post.withAccountAndImageFiles")
    Page<Post> findAll(Pageable pageable);

}
