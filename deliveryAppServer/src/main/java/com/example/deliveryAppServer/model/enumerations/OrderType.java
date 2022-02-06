package com.example.deliveryAppServer.model.enumerations;

import java.io.Serializable;

/**
 * List of all possible order that the application manages
 */
public enum OrderType implements Serializable {
    TAKE_AWAY,
    DELIVERY,
    DELIVERY_RIDERS,
    DELIVERY_NORIDER
}
