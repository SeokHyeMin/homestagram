package com.home.inmy.post;

import com.home.inmy.postTag.PostTag;
import com.home.inmy.tag.Tag;
import com.home.inmy.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class Post_TagService {

    private final PostRepository postRepository;
    private final TagService tagService;

    public void addPostTag(Post post, Tag tags){

        PostTag postTag = new PostTag();
        postTag.setPost(post);
        postTag.setTag(tags);

    }


}
