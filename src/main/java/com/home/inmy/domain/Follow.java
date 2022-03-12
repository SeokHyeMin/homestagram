package com.home.inmy.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name="follow",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "follow_uk",
                        columnNames={"fromAccountId","toAccountId"}
                )
        }
) //중복으로 될수있는걸 막기
public class Follow {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fromAccountId")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "toAccountId")
    private Account toAccount;
}
