package com.home.inmy.service;

import com.home.inmy.domain.entity.Post;
import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

public interface PostTagService {

    void postTagSave(Post post, Tag tag);

    void tagSave(Post post, String tags) throws JSONException;

    void deleteTag(PostTag postTag);

    void deletePost(Post post);

    List<PostTag> getPostTagList(Post post);
}
