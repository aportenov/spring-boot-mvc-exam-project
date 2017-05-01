package com.volunteers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such volunteer!")
public class VolunteerNotFoundExeption extends RuntimeException {

    public VolunteerNotFoundExeption(String volunteerNotFound) {
        super(volunteerNotFound);
    }
}
