package com.home.inmy.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "account_num") @ToString
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

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    //프로필
    private String bio;

    public void generateEmailToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp(){
        this.emailCheckVerified = true;
        this.joinedAt = LocalDateTime.now();
    }
}
