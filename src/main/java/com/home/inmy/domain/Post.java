package com.home.inmy.domain;

import lombok.*;
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
    private Long post_num;

    @Length(min = 2, max = 20)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_num")
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

    public void updateViews(){
        views++;
    } //조회수 증가
}
