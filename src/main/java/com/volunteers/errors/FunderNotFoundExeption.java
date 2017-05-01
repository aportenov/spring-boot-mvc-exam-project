package com.volunteers.errors;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such funder!")
public class FunderNotFoundExeption extends RuntimeException {

    public FunderNotFoundExeption(String funderNotFound) {
        super(funderNotFound);
    }
}
