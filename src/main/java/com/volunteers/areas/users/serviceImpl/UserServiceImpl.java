package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.users.services.UserService;
import com.volunteers.areas.users.entities.AbstractUser;
import com.volunteers.errors.Errors;
import com.volunteers.areas.users.repositories.AbstractUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
public class UserServiceImpl implements UserService {

    private final AbstractUserRepository abstractUserRepository;

    @Autowired
    public UserServiceImpl(AbstractUserRepository abstractUserRepository) {
        this.abstractUserRepository = abstractUserRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AbstractUser user = this.abstractUserRepository.findOneByUsername(email);
        if(user == null){
            throw new UsernameNotFoundException(Errors.INVALID_CREDENTIALS);
        }

        return user;
    }

}
