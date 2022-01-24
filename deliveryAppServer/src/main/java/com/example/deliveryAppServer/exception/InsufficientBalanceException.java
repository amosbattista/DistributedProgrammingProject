package com.example.deliveryAppServer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException(){super("Balance is insufficient to complete the operation");}

    public InsufficientBalanceException(String message){super(message);}
}
