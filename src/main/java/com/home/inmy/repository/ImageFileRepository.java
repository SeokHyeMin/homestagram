package com.home.inmy.repository;

import com.home.inmy.domain.entity.ImageFile;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    List<ImageFile> findByPost(Post post);

    void deleteByPostAndId(Post post, Long id);
}
