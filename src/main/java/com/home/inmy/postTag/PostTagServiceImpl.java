package com.home.inmy.postTag;

import com.home.inmy.domain.Post;
import com.home.inmy.domain.PostTag;
import com.home.inmy.domain.Tag;
import com.home.inmy.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostTagServiceImpl implements PostTagService{

    private final PostTagRepository postTagRepository;

    private final TagService tagService;

    @Override
    public void tagSave(Post post, String tags) throws JSONException {

        JSONArray jsonArray = new JSONArray(tags);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tagList = jsonArray.getJSONObject(i);
            String tagTitle = tagList.getString("value");
            Tag tag = tagService.findOrCreateNew(tagTitle);
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
    public void deleteTag(PostTag postTag) {
        postTagRepository.delete(postTag);
    }

    @Override
    public void deletePost(Post post) {
        postTagRepository.deleteInBatch(post.getPostTags());
    }

    @Override
    public List<PostTag> getPostTagList(Post post) {

        return  postTagRepository.findByPost(post);
    }
}
