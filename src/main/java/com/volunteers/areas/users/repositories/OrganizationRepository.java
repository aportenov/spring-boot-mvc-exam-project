package com.volunteers.areas.users.repositories;

import com.volunteers.areas.users.entities.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends UserRepository<Organization> {
}
