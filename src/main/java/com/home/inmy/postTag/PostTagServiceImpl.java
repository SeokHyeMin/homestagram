package com.home.inmy.postTag;

import com.home.inmy.domain.Post;
import com.home.inmy.domain.PostTag;
import com.home.inmy.domain.Tag;
import com.home.inmy.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Page<PostTag> searchPostByTag(Tag tag, int page){

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id"));

        return postTagRepository.findByTag(tag,pageRequest);
    }

    public Page<PostTag> searchPostByTag(Tag tag, int page, String orderBy) {

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "post."+orderBy));

        return postTagRepository.findByTag(tag,pageRequest);
    }
}
