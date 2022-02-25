package com.home.inmy.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "tag_num")
public class Tag {

    @Id @GeneratedValue
    private Long tag_num;

    @Column(unique = true, nullable = false)
    private String tagTitle;
}
