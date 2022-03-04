package com.home.inmy.images;

import com.home.inmy.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    List<ImageFile> findByPost(Post post);
}
