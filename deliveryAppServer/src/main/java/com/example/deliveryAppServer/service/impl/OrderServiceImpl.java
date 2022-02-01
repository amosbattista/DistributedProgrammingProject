package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.OrderStateGraph.OrderStateGraph;
import com.example.deliveryAppServer.exception.*;
import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.dao.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.repository.DishRepository;
import com.example.deliveryAppServer.repository.OrderRepository;
import com.example.deliveryAppServer.repository.RiderRepository;
import com.example.deliveryAppServer.service.CustomerService;
import com.example.deliveryAppServer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.deliveryAppServer.model.enumerations.OrderState.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CustomerService customerService;

    private OrderStateGraph orderStateGraph;

    @Autowired
    private RiderRepository riderRepository;

    @Override
    public OrderEntity createNewOrder(OrderEntity order) {


        if(orderRepository.existsByCustomerIdAndOrderStateNotIn(order.getCustomer().getId(), List.of(COMPLETED, REFUSED))){
            throw new OrderAlreadyInProgress();
        }

        order.setOrderState(PENDING);
        order = orderRepository.save(order);

        List<DishOrderAssociation> doaList = order.getDishOrderAssociations();
        Double total = 0.0;
        for(DishOrderAssociation doa : doaList){
            doa.setOrder(order);
            total+=dishRepository.findById(doa.getDish().getId()).get().getPrice() * doa.getQuantity();
        }

        order.setPrice(total);

        try{
            customerService.updateBalance(-total, order.getCustomer().getId());
        }
        catch(InsufficientBalanceException ex){
            //order.setOrderState(REFUSED);
           // orderRepository.save(order);

            orderRepository.delete(order);
            throw new InsufficientBalanceException("Order REFUSED for insufficient balance");
        }

        try{
            order = orderRepository.save(order);
        }
        catch (Exception ex){
            orderRepository.deleteById(order.getId());
            customerService.updateBalance(total, order.getCustomer().getId());
            throw new ServerError("Order REFUSED");

        }

        return order;


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
        if(!(order.getOrderState().equals(PENDING) && order.getOrderType().equals(OrderType.DELIVERY) && (orderType.equals(OrderType.DELIVERY_NORIDER) || orderType.equals(OrderType.DELIVERY_RIDERS))))
            throw new IllegalOrderType();

        order.setOrderType(orderType);
        orderRepository.save(order);

    }

    public OrderEntity getCurrentOrder(Long customerId){
        OrderEntity order = orderRepository.findByCustomerIdAndOrderStateNotIn(customerId, List.of(COMPLETED, REFUSED));
        if(order == null){
            throw new OrderNotFound("No current order found");
        }


        return order;
    }

    public void setRiderOrder(Long orderId, Long riderId){

        RiderEntity rider = riderRepository.getById(riderId);
        OrderEntity order;

        if(rider == null)
            throw new UserNotFound();

        try {
            order = orderRepository.findById(orderId).get();
        }catch (NoSuchElementException ex){
            throw new OrderNotFound();
        }

        order.setRider(rider);

        orderRepository.save(order);





    }
}
