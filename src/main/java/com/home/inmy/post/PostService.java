package com.home.inmy.post;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.images.ImageFile;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post newPostSave(Post post, Account account, List<ImageFile> imageFiles){

        Post newPost = postRepository.save(post);
        newPost.setAuthor(account.getLoginId());
        newPost.setImageFiles(new ArrayList<>(imageFiles));
        newPost.completePostSave();
        return newPost;
    }

    public PostDto getPost(Long post_num){

        Post post = postRepository.findById(post_num).get();

        return PostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .post_num(post.getPost_num())
                .build();
    }

}
