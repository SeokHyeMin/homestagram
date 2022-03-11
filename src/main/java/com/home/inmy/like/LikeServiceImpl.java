package com.home.inmy.like;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Likes;
import com.home.inmy.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;

    @Override
    public void addLike(Post post, Account account) {
        Likes like = Likes.builder()
                .account(account)
                .post(post)
                .build();

        likeRepository.save(like);
    }

    @Override
    public void removeLike(Post post, Account account) {
        Likes likes = likeRepository.findByPostAndAccount(post, account);
        likeRepository.delete(likes);
    }

    @Override
    public void deletePostLike(Post post) {
        likeRepository.deleteAll(post.getLikesList());
    }

    @Override
    public boolean accountPostLike(Post post , Account account){
        Likes likes = likeRepository.findByPostAndAccount(post, account);

        return likes != null;
    }

    @Override
    public List<Long> getLikePostNum(List<Likes> likes) {

        List<Long> postNumList = new ArrayList<>();

        for (Likes like : likes) {
            postNumList.add(like.getPost().getPost_num());
        }
        return postNumList;
    }

    @Override
    public List<Likes> getLikeList(Account account) {

        return likeRepository.findByAccount(account);
    }

}
