package com.example.deliveryAppServer.model.dto.order;

import com.example.deliveryAppServer.model.dao.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import com.example.deliveryAppServer.model.dto.user.CustomerDto;
import com.example.deliveryAppServer.model.dto.user.ProviderDto;
import com.example.deliveryAppServer.model.dto.user.RiderDto;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    private Long id;

    private List<DishOrderAssociation> dishOrderAssociations;

    private CustomerDto customer;

    private ProviderDto provider;

    private RiderDto rider;

    private OrderType orderType;

    private OrderState orderState;

    private LocalDateTime deliveryTime;

    private double price;


}