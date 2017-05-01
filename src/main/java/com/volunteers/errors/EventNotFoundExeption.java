package com.volunteers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such event!")
public class EventNotFoundExeption extends RuntimeException {

    public EventNotFoundExeption(String eventNotfound) {
        super(eventNotfound);
    }

}
