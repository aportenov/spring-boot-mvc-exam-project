package com.volunteers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such organization!")
public class OrganizationNotFoundExeption extends RuntimeException {

    public OrganizationNotFoundExeption(String invalidOrganization) {
        super(invalidOrganization);
    }
}
