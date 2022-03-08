package com.home.inmy.postTag;

import com.home.inmy.domain.Post;
import com.home.inmy.domain.PostTag;
import com.home.inmy.domain.Tag;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface PostTagService {

    void postTagSave(Post post, Tag tag);

    void tagSave(Post post, String tags) throws JSONException;

    void deleteTag(PostTag postTag);

    void deletePost(Post post);
}
