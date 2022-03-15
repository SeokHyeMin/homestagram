package com.home.inmy.post;

import com.home.inmy.bookmark.BookmarkServiceImpl;
import com.home.inmy.domain.Post;
import com.home.inmy.images.ImageFileRepository;
import com.home.inmy.images.ImageFileService;
import com.home.inmy.images.ImageFileServiceImpl;
import com.home.inmy.like.LikeService;
import com.home.inmy.like.LikeServiceImpl;
import com.home.inmy.postTag.PostTagService;
import com.home.inmy.postTag.PostTagServiceImpl;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final ImageFileRepository imageFileRepository;
    private final PostRepository postRepository;

    private final PostTagServiceImpl postTagService;
    private final ImageFileServiceImpl imageFileService;
    private final LikeServiceImpl likeService;
    private final BookmarkServiceImpl bookmarkService;

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
    public void deletePost(Long post_num) { //post와 연관되어 있는 것들 함께 삭제
        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        imageFileService.deleteImageFile(post);
        likeService.deletePostLike(post);
        bookmarkService.deletePostBookmark(post);
        postTagService.deletePost(post);
        postRepository.delete(post);
    }

    @Override
    public Post getPost(Long post_num) {

        return postRepository.findById(post_num).orElseThrow(()->new IllegalArgumentException("해당 글이 없습니다."));
    }

    public Post HighestViews(){

        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        List<Post> postList = postRepository.findAll(sort);

        if(postList.isEmpty()){
            return null;
        }

        return postList.get(0) ;
    }

    public List<Post> DescLikes(){

        Sort sort = Sort.by(Sort.Direction.DESC, "likesList");

        return postRepository.findAll(sort);
    }

    public List<Post> DescBookmark(){

        Sort sort = Sort.by(Sort.Direction.DESC, "bookmarkList.size()");

        return postRepository.findAll(sort);
    }

    public Page<Post> pageList(int page){

        return postRepository.findAll(PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "writeTime")));
    }

}
