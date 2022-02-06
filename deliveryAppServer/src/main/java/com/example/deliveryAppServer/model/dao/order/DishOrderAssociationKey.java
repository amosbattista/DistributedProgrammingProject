package com.example.deliveryAppServer.model.dao.order;


import java.io.Serializable;

/**
 * It's a class used to define the multiple primary key of the DishOrderAssociation class.
 */
public class DishOrderAssociationKey implements Serializable {
    private DishEntity dish;
    private OrderEntity order;
}
