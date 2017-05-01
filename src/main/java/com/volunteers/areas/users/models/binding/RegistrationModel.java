package com.volunteers.areas.users.models.binding;

import com.volunteers.anotations.EmailExistsConstraint;
import com.volunteers.anotations.IsPasswordsMatching;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Pattern;

@IsPasswordsMatching
public class RegistrationModel {

    @EmailExistsConstraint
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = "Password must contains upper letter, lower letter and digits")
    private String password;

    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
