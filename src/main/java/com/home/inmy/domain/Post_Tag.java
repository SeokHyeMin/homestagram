package com.home.inmy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post_Tag {

    @Id @GeneratedValue
    private Long postDetails_num;

    @ManyToOne
    @JoinColumn(name = "post_num")
    private Post post;


    @ManyToOne
    @JoinColumn(name = "tag_num")
    private Tag tag;
}
