package com.home.inmy.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "tag_num")
public class Tag{

    @Id @GeneratedValue
    private Long tag_num;

    @Column(unique = true, nullable = false)
    private String tagTitle;

    @OneToMany(mappedBy = "tag")
    private Set<Post_Tag> post_tags = new HashSet<>();
}
