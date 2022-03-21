package com.home.inmy.domain;

import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Length(min = 2, max = 20)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<ImageFile> imageFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Bookmark> bookmarkList = new ArrayList<>();

    private String content;

    private LocalDateTime writeTime;

    private String category;

    private String writer;

    private Long views; //조회수

    private Long likes; //좋아요 수

    public void updateViews(){
        views++;
    } //조회수 증가

    public void increaseLikes(){likes++;}

    public void decreaseLikes(){likes--;}
}
