package com.home.inmy.repository;

import com.home.inmy.domain.entity.Post;
import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPost(Post post);

    @EntityGraph(value = "PostTag.withPost")
    Page<PostTag> findByTag(Tag tag, Pageable pageable);

    PostTag findByPostAndTag(Post post, Tag tag);

}
