package com.home.inmy.domain;

import com.home.inmy.domain.PostTag;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Tag{

    @Id @GeneratedValue
    private Long tag_num;

    @Column(unique = true, nullable = false)
    private String tagTitle;

    @OneToMany(mappedBy = "tag")
    private Set<PostTag> postTags = new HashSet<>();
}
