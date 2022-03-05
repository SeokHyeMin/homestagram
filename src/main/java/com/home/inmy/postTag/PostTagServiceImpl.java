package com.home.inmy.postTag;

import com.home.inmy.post.Post;
import com.home.inmy.tag.Tag;
import com.home.inmy.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostTagServiceImpl implements PostTagService{

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    @Override
    public void tagSave(Post post, String tags) throws JSONException {

        JSONArray jsonArray = new JSONArray(tags);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tagList = jsonArray.getJSONObject(i);
            String tagTitle = tagList.getString("value");
            Tag newTag = tagRepository.findByTagTitle(tagTitle);
            postTagSave(post, newTag);
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
