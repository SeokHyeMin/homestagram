package com.home.inmy.postTag;

import com.home.inmy.post.Post;
import com.home.inmy.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPost(Post post);
}
