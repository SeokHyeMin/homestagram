package com.home.inmy.service;

import com.home.inmy.domain.entity.Role;

import java.util.List;

public interface RoleService {

    Role createRoleIfNotFound(String roleName);

    Role getRole(long id);

    List<Role> getRoles();

    void createRole(Role role);

    void deleteRole(long id);

}
