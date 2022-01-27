package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExists extends RuntimeException{


    public UserAlreadyExists() {
        super("Trying to generate an entity that already exist in DB.");
    }

    public UserAlreadyExists(String message) {
        super(message);
    }
}
