package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DishNotFound extends RuntimeException{


    public DishNotFound() {
        super("Trying to find a dish that does not exist in DB.");
    }

    public DishNotFound(String message) {
        super(message);
    }
}
