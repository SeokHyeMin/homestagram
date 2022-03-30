package com.home.inmy.repository;

import com.home.inmy.domain.entity.AccountRole;
import com.home.inmy.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    AccountRole findByRole(Role role);
}
