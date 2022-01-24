package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCredentials extends RuntimeException{

    public InvalidCredentials(){super("Invalid username or password.");}

    public InvalidCredentials(String message){super(message);}

}
