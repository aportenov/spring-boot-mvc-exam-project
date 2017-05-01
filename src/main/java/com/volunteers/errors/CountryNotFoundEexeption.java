package com.volunteers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such country!")
public class CountryNotFoundEexeption extends RuntimeException {

    public CountryNotFoundEexeption(String countryNotFound) {
        super(countryNotFound);
    }
}
