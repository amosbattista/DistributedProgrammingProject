package com.example.deliveryAppServer.model.dto.order;

import com.example.deliveryAppServer.model.dao.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.dto.user.CustomerDto;
import com.example.deliveryAppServer.model.dto.user.ProviderDto;
import com.example.deliveryAppServer.model.dto.user.RiderDto;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
/**
 * This is a utility class used to store order information that must be sent or received by the server.
 * This class should be used by those who do not create the order and therefore do not have to specify all the
 * information necessary for its creation on the server
 */
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
