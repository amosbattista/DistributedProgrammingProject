package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServerError extends RuntimeException{


    public ServerError() {
        super("Something went wrong...");
    }

    public ServerError(String message) {
        super(message);
    }
}