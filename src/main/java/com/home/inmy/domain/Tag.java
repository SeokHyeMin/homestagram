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
public class Tags {

    @Id @GeneratedValue
    private Long tag_num;

    @Column(unique = true, nullable = false)
    private String tagTitle;

    @OneToMany(mappedBy = "tags")
    private Set<PostDetails> postDetailsSet = new HashSet<>();
}
