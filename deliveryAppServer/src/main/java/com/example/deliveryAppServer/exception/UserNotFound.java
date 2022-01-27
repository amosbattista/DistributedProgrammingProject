package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException{


    public UserNotFound() {
        super("Trying to find an user that does not exist in DB.");
    }

    public UserNotFound(String message) {
        super(message);
    }
}
