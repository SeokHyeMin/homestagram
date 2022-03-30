package com.home.inmy.domain.entity;

import lombok.*;

import javax.persistence.*;

@NamedEntityGraph(name = "Follow.withAll", attributeNodes = {
        @NamedAttributeNode("fromAccount"),
        @NamedAttributeNode("toAccount"),
})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccountId")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccountId")
    private Account toAccount;
}
