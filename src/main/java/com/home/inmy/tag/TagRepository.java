package com.home.inmy.tag;

import com.home.inmy.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTagTitle(String tagTitle);
}