package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderAlreadyInProgress extends RuntimeException{

    public OrderAlreadyInProgress(){super("You already have an order in progress");}

    public OrderAlreadyInProgress(String message){super(message);}
}
