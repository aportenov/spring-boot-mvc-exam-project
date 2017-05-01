package com.volunteers.areas.users.repositories;

import com.volunteers.areas.users.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{

    Role findOneByAuthority(String defaultRole);
}
