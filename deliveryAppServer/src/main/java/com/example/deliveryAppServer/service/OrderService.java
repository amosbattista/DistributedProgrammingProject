package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;

import java.util.List;

public interface OrderService {
    public OrderEntity createNewOrder(OrderEntity order);
    public OrderEntity getOrderState(Long orderId);
    public List<OrderEntity> getOrdersByState(OrderState orderState);
    public List<OrderEntity> getCustomerOrdersHistory(Long customerId);
    public List<OrderEntity> getProviderOrdersByState(Long providerId, OrderState orderState);

    public void changeOrderState(Long orderId, OrderState orderState);
    public void changeOrderType(Long orderId, OrderType orderType);

    public OrderEntity getCurrentOrder(Long customerId);

    public void setRiderOrder(Long orderId, Long riderId);
    public void refuseExpiredOrders();

    public List<OrderEntity> getAtLeastAcceptedOrdersByRider(Long riderId);
}
