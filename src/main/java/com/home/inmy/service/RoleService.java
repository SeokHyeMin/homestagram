package com.home.inmy.service;

import com.home.inmy.domain.entity.Role;

public interface RoleService {

    Role createRoleIfNotFound(String roleName, String roleDesc);

}
