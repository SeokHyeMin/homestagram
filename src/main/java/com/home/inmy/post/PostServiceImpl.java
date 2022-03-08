package com.home.inmy.post;

import com.home.inmy.domain.Post;
import com.home.inmy.images.ImageFileRepository;
import com.home.inmy.images.ImageFileService;
import com.home.inmy.like.LikeService;
import com.home.inmy.postTag.PostTagService;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final ImageFileRepository imageFileRepository;
    private final PostRepository postRepository;

    private final PostTagService postTagService;
    private final ImageFileService imageFileService;
    private final LikeService likeService;

    private final ModelMapper modelMapper;

    public Post newPostSave(PostDto postDto) throws IOException {

        Post newPost = postDto.createPost();
        imageFileService.saveImageFile(newPost, postDto.getImageFiles());

        return postRepository.save(newPost);
    }

    public Post updatePost(PostDto postDto, Long post_num) throws IOException {

        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        modelMapper.map(postDto, post); //변경감지

        imageFileRepository.deleteAll(post.getImageFiles());
        imageFileService.saveImageFile(post, postDto.getImageFiles());

        return post;
    }

    @Override
    public void updateViews(Post post) {
        post.updateViews();
    }

    @Override
    public void increaseLikes(Post post) {
        post.increaseLikes();
    }

    @Override
    public void decreaseLikes(Post post) {
        if(post.getLikes() != 0){
            post.decreaseLikes();
        }
    }

    @Override
    public void deletePost(Long post_num) {
        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        imageFileService.deleteImageFile(post);
        likeService.deletePostLike(post);
        postTagService.deletePost(post);
        postRepository.delete(post);
    }

}
