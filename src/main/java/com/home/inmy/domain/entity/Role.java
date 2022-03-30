package com.home.inmy.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Role{

    @Id @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @OneToMany(mappedBy = "accountRole")
    private Set<Account> accounts = new HashSet<>();

}


