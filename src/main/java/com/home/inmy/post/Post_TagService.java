package com.home.inmy.post;

import com.home.inmy.domain.Post;
import com.home.inmy.domain.Post_Tag;
import com.home.inmy.domain.Tag;
import com.home.inmy.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class Post_TagService {

    private final PostRepository postRepository;
    private final TagService tagService;

    public void addPostTag(Post post, Tag tags){

        Post_Tag postTag = new Post_Tag();
        postTag.setPost(post);
        postTag.setTag(tags);

    }


}
