package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalOrderState extends RuntimeException{


    public IllegalOrderState() {
        super("Wrong value for order state");
    }

    public IllegalOrderState(String message) {
        super(message);
    }
}