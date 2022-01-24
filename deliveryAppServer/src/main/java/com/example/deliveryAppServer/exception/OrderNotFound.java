package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFound extends RuntimeException{


    public OrderNotFound() {
        super("Trying to find an order that does not exist in DB.");
    }

    public OrderNotFound(String message) {
        super(message);
    }
}