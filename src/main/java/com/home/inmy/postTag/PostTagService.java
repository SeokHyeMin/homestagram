package com.home.inmy.postTag;

import com.home.inmy.post.Post;
import com.home.inmy.tag.Tag;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface PostTagService {

    void PostTagSave(Post post);

    Long PostTagSave(Tag tag);

    Set<Tag> getTags(Post post);

}
