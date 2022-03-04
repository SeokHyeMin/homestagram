package com.home.inmy.postTag;

import com.home.inmy.post.Post;
import com.home.inmy.tag.Tag;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PostTagServiceImpl implements PostTagService{

    public void PostTagSave(Post post, Tag tag) {
            PostTag postTag = new PostTag();
            postTag.setPost(post);
            postTag.setTag(tag);
    }

    @Override
    public void PostTagSave(Post post) {

    }

    @Override
    public Long PostTagSave(Tag tag) {
        PostTag postTag = new PostTag();
        postTag.setTag(tag);

        return postTag.getId();
    }

    @Override
    public Set<Tag> getTags(Post post) {
        return null;
    }
}
