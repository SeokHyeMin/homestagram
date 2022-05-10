package com.home.inmy.repository;

import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @EntityGraph(value = "PostTag.withPost")
    Page<PostTag> findByTag(Tag tag, Pageable pageable);

}
