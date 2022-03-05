package com.home.inmy.postTag;

import com.home.inmy.post.Post;
import com.home.inmy.tag.Tag;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.Set;

public interface PostTagService {

    void postTagSave(Post post, Tag tag);

    void tagSave(Post post, String tags) throws JSONException;

    Long PostTagSave(Tag tag);

    Set<Tag> getTags(Post post);

}
