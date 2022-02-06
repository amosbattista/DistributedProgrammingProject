package com.example.deliveryAppServer.model.enumerations;

import java.io.Serializable;

/**
 * List of all possible states in which an order can be found during the execution of the application
 */
public enum OrderState implements Serializable {

    PENDING,
    ACCEPTED,
    SHIPPED,
    COMPLETED,
    SEMI_ACCEPTED,
    REFUSED

}
