package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.Role;
import com.home.inmy.repository.RoleRepository;
import com.home.inmy.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRoleIfNotFound(String roleName, String roleDesc) {

        Role role = roleRepository.findByRoleName(roleName);

        //해당하는 권한이 없다면 생성해서 반환.
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
            return roleRepository.save(role);
        }
        return role;
    }

}
