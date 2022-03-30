package com.home.inmy.repository;

import com.home.inmy.domain.entity.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTagTitle(String tagTitle);

}
