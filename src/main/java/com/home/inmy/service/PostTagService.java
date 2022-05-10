package com.home.inmy.service;

import com.home.inmy.domain.entity.Post;
import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;

public interface PostTagService {

    void postTagSave(Post post, Tag tag);

    void tagSave(Post post, String tags) throws JSONException;

    void deletePost(Post post);

    Page<PostTag> searchPostByTag(Tag tag, int page, String orderBy);
}
