package com.volunteers.areas.users.repositories;

import com.volunteers.areas.users.entities.AbstractUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AbstractUserRepository extends UserRepository<AbstractUser> {

}
