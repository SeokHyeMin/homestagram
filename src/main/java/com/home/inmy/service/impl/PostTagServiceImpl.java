package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.Post;
import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import com.home.inmy.repository.PostTagRepository;
import com.home.inmy.service.PostTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostTagServiceImpl implements PostTagService {

    private final PostTagRepository postTagRepository;

    private final TagServiceImpl tagService;

    @Override
    public void tagSave(Post post, String tags) throws JSONException {

        JSONArray jsonArray = new JSONArray(tags);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tagList = jsonArray.getJSONObject(i);
            String tagTitle = tagList.getString("value");
            Tag tag = tagService.findOrCreateNew(tagTitle); //해당 태그를 찾고, 없으면 생성 후 반환
            postTagSave(post, tag);
        }

    }

    @Override
    public void postTagSave(Post post, Tag tag) {
        PostTag postTag = new PostTag();
        postTag.setPost(post);
        postTag.setTag(tag);
        postTagRepository.save(postTag);
    }

    @Override
    public void deletePost(Post post) {
        postTagRepository.deleteInBatch(post.getPostTags());
    }

    @Override
    public List<PostTag> getPostTagList(Post post) {

        return  postTagRepository.findByPost(post);
    }

    @Override
    public Page<PostTag> searchPostByTag(Tag tag, int page, String orderBy) {

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "post."+orderBy));

        return postTagRepository.findByTag(tag,pageRequest);
    }
}
