package com.home.inmy.repository;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Comments;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    @EntityGraph(value = "Comment.withAccount")
    Page<Comments> findAllByPost(Post post, Pageable pageable);

    void deleteAllByPost(Post post);

    void deleteAllByAccount(Account account);
}
