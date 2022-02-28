package com.home.inmy.post;

import com.home.inmy.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

}
