package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalOrderType extends RuntimeException{


    public IllegalOrderType() {
        super("Wrong value for order type");
    }

    public IllegalOrderType(String message) {
        super(message);
    }
}
