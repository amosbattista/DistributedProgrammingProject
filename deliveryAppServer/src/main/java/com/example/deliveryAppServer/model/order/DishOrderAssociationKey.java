package com.example.deliveryAppServer.model.order;


import java.io.Serializable;

public class DishOrderAssociationKey implements Serializable {
    private DishEntity dish;
    private OrderEntity order;
}
