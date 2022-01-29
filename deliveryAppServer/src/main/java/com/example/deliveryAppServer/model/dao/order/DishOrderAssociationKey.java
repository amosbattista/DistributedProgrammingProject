package com.example.deliveryAppServer.model.dao.order;


import java.io.Serializable;

public class DishOrderAssociationKey implements Serializable {
    private DishEntity dish;
    private OrderEntity order;
}
