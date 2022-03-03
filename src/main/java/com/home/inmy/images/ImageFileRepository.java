package com.home.inmy.images;

import com.home.inmy.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    List<ImageFile> findByPost(Long post_num);
}
