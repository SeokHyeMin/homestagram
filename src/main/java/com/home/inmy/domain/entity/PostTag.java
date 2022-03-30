package com.home.inmy.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NamedEntityGraph(name = "PostTag.withPost", attributeNodes = {
        @NamedAttributeNode(value = "post", subgraph = "post")
    },
        subgraphs = @NamedSubgraph(name = "post", attributeNodes = {
                @NamedAttributeNode("account"),
                @NamedAttributeNode("imageFiles")
        })
)
@Entity
@Getter @Setter
@NoArgsConstructor
public class PostTag {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_num")
    private Tag tag;
}
