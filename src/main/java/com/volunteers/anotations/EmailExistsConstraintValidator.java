package com.volunteers.anotations;

import com.volunteers.areas.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailExistsConstraintValidator implements ConstraintValidator<EmailExistsConstraint, String> {

    @Autowired
    private UserService userService;

    public void initialize(EmailExistsConstraint constraint) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
       try {
           UserDetails isUsernameFree = userService.loadUserByUsername(email);
       }catch (UsernameNotFoundException e){
            return true;
       }

        return false;

    }
}
