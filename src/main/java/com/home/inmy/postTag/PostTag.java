package com.home.inmy.postTag;

import com.home.inmy.post.Post;
import com.home.inmy.tag.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PostTag {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_num")
    private Post post;


    @ManyToOne
    @JoinColumn(name = "tag_num")
    private Tag tag;
}
