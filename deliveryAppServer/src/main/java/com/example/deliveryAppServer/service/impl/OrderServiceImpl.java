package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.OrderStateGraph.OrderStateGraph;
import com.example.deliveryAppServer.exception.IllegalOrderState;
import com.example.deliveryAppServer.exception.IllegalOrderType;
import com.example.deliveryAppServer.exception.OrderNotFound;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.order.OrderEntity;
import com.example.deliveryAppServer.repository.OrderRepository;
import com.example.deliveryAppServer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.deliveryAppServer.model.enumerations.OrderState.PENDING;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishOrderAssociationRepository dishOrderAssociationRepository;

    private OrderStateGraph orderStateGraph;

    @Override
    public void createNewOrder(OrderEntity order) {
        order.setOrderState(PENDING);
        order = orderRepository.save(order);

        List<DishOrderAssociation> doaList = order.getDishOrderAssociations();
        for(DishOrderAssociation doa : doaList){
            doa.setOrder(order);
        }
        orderRepository.save(order);

    }

    @Override
    public OrderEntity getOrderState(Long orderId) {
        OrderEntity order;
        try {
            order = orderRepository.findById(orderId).get();
        }catch (NoSuchElementException ex){
            throw new OrderNotFound();
        }
        return order;

    }

    @Override
    public List<OrderEntity> getOrdersByState(OrderState orderState){
        return orderRepository.findAllByOrderState(orderState);
    }

    @Override
    public List<OrderEntity> getCustomerOrdersHistory(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);

    }

    @Override
    public List<OrderEntity> getProviderOrdersByState(Long providerId, OrderState orderState) {
        return orderRepository.findAllByProviderIdAndOrderState(providerId, orderState);
    }

    @Override
    public void changeOrderState(Long orderId, OrderState orderState) {
        OrderEntity order;
        try {
            order = orderRepository.findById(orderId).get();
        }catch (NoSuchElementException ex){
            throw new OrderNotFound();
        }
        orderStateGraph = OrderStateGraph.getOrderStateGraphInstance();
        if(OrderStateGraph.getOrderStateGraphInstance().checkNextState(order.getOrderState(), orderState, order.getOrderType())){
            order.setOrderState(orderState);
            orderRepository.save(order);
        }
        else
            throw new IllegalOrderState();

    }

    @Override
    public void changeOrderType(Long orderId, OrderType orderType) {
        OrderEntity order;
        try {
            order = orderRepository.findById(orderId).get();
        }catch (NoSuchElementException ex){
            throw new OrderNotFound();
        }
        if(!(order.getOrderType().equals(OrderType.DELIVERY) && (orderType.equals(OrderType.DELIVERY_NORIDER) || orderType.equals(OrderType.DELIVERY_RIDERS))))
            throw new IllegalOrderType();

        order.setOrderType(orderType);
        orderRepository.save(order);

    }
}
