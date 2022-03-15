package com.home.inmy.post;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findByAccount(Account account);

    List<Post> findAll(Sort sort);
}
