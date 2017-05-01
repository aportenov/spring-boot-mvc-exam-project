package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.users.entities.Role;
import com.volunteers.areas.users.services.RoleService;
import com.volunteers.enumerators.Roles;
import com.volunteers.areas.users.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefaultRole() {
        return this.roleRepository.findOneByAuthority(String.valueOf(Roles.ROLE_USER));
    }

    @Override
    public Role getOrganizationRole() {return this.roleRepository.findOneByAuthority(String.valueOf(Roles.ROLE_ORGANIZATION));}

    @Override
    public Role getFunderRole() {return this.roleRepository.findOneByAuthority(String.valueOf(Roles.ROLE_FUNDER));}

    @Override
    public Role getSocialRole() {
        return this.roleRepository.findOneByAuthority(String.valueOf(Roles.ROLE_SOCIAL));
    }


}
