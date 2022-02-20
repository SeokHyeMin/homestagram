package com.home.inmy.domain;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@Builder
@Getter
@ToString
public class Profile {

    private String bio;

    private String url;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;
    
}
