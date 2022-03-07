package com.home.inmy.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor(access = AccessLevel.PROTECTED) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    //회원가입
    @Id @GeneratedValue
    private Long account_num;

    @Column(unique = true)
    private String nickname;

    private String dateToBirth;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String phoneNumber;

    //이메일 체크
    private boolean emailCheckVerified;

    private String emailCheckToken;

    private LocalDateTime e_tokenGeneratedAt;

    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Likes> likes = new ArrayList<>();

    //프로필
    @Embedded
    private Profile profile = new Profile();

    public void generateEmailToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.e_tokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp(){
        this.emailCheckVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public Profile getProfile(){
        return this.profile == null ? new Profile() : this.profile;
    }
}
