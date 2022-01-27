package com.example.deliveryAppServer.model.enumerations;

import java.io.Serializable;

public enum OrderState implements Serializable {

    PENDING,
    ACCEPTED,
    SHIPPED,
    COMPLETED,
    SEMI_ACCEPTED,
    REFUSED

}
