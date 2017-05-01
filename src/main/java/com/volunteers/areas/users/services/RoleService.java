package com.volunteers.areas.users.services;

import com.volunteers.areas.users.entities.Role;


public interface RoleService{
    Role getDefaultRole();

    Role getOrganizationRole();

    Role getFunderRole();

    Role getSocialRole();
}

