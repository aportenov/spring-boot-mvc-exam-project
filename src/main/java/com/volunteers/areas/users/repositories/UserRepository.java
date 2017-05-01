package com.volunteers.areas.users.repositories;

import com.volunteers.areas.users.entities.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface UserRepository<T extends AbstractUser> extends JpaRepository<T , Long>{

    T findOneByUsername(String user);

}

