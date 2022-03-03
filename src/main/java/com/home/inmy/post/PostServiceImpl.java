package com.home.inmy.post;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.domain.ImageFile;
import com.home.inmy.images.ImageFileService;
import com.home.inmy.post.form.PostForm;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final EntityManager em;
    private final ImageFileService imageFileService;

    public Post newPostSave(PostDto postDto) throws IOException {

        Post newPost = postDto.createPost();
        List<ImageFile> imageFiles = imageFileService.saveImageFile(newPost, postDto.getImageFiles());

        return postRepository.save(newPost);
    }



    /*public PostForm getPost(Long post_num){

        Post post = postRepository.findById(post_num).get();

        return PostForm.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .post_num(post.getPost_num())
                .build();
    }*/

}
