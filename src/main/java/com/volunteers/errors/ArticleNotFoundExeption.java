package com.volunteers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such article!")
public class ArticleNotFoundExeption extends RuntimeException {

    public ArticleNotFoundExeption(String articleNotFound) {
        super(articleNotFound);
    }
}
