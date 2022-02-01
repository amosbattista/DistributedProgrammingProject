package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DishAlreadyPresent extends RuntimeException{


    public DishAlreadyPresent() {
        super("Dish already present!");
    }

    public DishAlreadyPresent(String message) {
        super(message);
    }
}
