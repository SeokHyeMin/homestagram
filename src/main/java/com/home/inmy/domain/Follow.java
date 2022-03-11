package com.home.inmy.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account toAccount;
}
