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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.deliveryAppServer.model.enumerations.OrderState.*;

/**
 * It defines and holds the app business logic, regarding the operations that act on the Order entity.
 * It performs CRUD queries on the database by using instance of the Order Repository, Dish Repository and
 * Rider Repository.
 */
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

    /**
     * It creates a new order into the database and calculate the final order price
     * @param order is the Order to be created
     * @return the OrderEntity just created
     * @throws OrderAlreadyInProgress if the customer (associated with the order) has already an in-progress order
     * @throws InsufficientBalanceException if the customer (associated with the order) does not have sufficient balance
     */
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
            //throw new ServerError("Order REFUSED");
            throw ex;

        }
        log.info("[SERVICE] New order "+order.getId()+" created!");
        return order;


    }

    /**
     * It retrieves from the databased the entire OrderEntity, given its id
     * @param orderId is the order ID
     * @throws OrderNotFound if the given order is not present on the database
     * @return the OrderEntity
     */
    @Override
    public OrderEntity getOrder(Long orderId) {
        OrderEntity order;
        try {
            order = orderRepository.findById(orderId).get();
        }catch (NoSuchElementException ex){
            throw new OrderNotFound();
        }
        log.info("[SERVICE] get order with id: "+orderId);
        return order;

    }

    /**
     * It retrieves from the database all the orders with the given order state
     * @param orderState is the order state
     * @return a List<OrderEntity>
     */
    @Override
    public List<OrderEntity> getOrdersByState(OrderState orderState){
        log.info("[SERVICE] get all order with state: "+orderState);
        return orderRepository.findAllByOrderState(orderState);
    }

    /**
     * It retrieves form the database all the orders of a given customer
     * @param customerId is the customer ID
     * @return List<OrderEntity>
     */
    @Override
    public List<OrderEntity> getCustomerOrdersHistory(Long customerId) {
        log.info("[SERVICE] get all order for customer: "+customerId);
        return orderRepository.findAllByCustomerId(customerId);

    }

    /**
     * It retrieves form the database all the orders of a given provider, having a given order state
     * @param providerId is the provider ID
     * @param orderState is the order state

     * @return List<OrderEntity>
     */
    @Override
    public List<OrderEntity> getProviderOrdersByState(Long providerId, OrderState orderState) {
        log.info("[SERVICE] get all provider orders with state: "+orderState);
        return orderRepository.findAllByProviderIdAndOrderState(providerId, orderState);
    }

    /**
     * It updates an existing order on database, if it exists, changing its state.
     * @param orderId is the order ID
     * @param orderState is the new state
     * @throws OrderNotFound if the given order is not present on the database
     * @throws IllegalOrderState if the given new state is incompatible with the previous order state
     */
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
        if(orderState.equals(REFUSED)){
            Long customerId = order.getCustomer().getId();
            Double refund = order.getPrice();
            customerService.updateBalance(refund, customerId);
        }

        log.info("[SERVICE] changing state in: "+orderState+" for order: "+orderId);


    }

    /**
     * It updates an existing order on database, if it exists, changing its type.
     * @param orderId is the order ID
     * @param orderType is the new type
     * @throws OrderNotFound if the given order is not present on the database
     * @throws IllegalOrderType if the given new state is incompatible with the previous order type
     */
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
        log.info("[SERVICE] changing type in: "+orderType+" for order: "+orderId);

    }

    /**
     * It retrieves from database the current order of a given customer
     * @param customerId is the customer ID
     * @throws OrderNotFound if no current order is associated to the given customer
     * @return the OrderEntity
     */
    public OrderEntity getCurrentOrder(Long customerId){
        OrderEntity order = orderRepository.findByCustomerIdAndOrderStateNotIn(customerId, List.of(COMPLETED, REFUSED));
        if(order == null){
            throw new OrderNotFound("No current order found");
        }
        log.info("[SERVICE] get current order for customer: "+customerId);
        return order;
    }

    /**
     * It updates an existing order on database, if it exists, setting its rider.
     * @param orderId is the order ID to update
     * @param riderId identify the new order rider
     * @throws UserNotFound if the given rider is not present on the database
     * @throws OrderNotFound if the given order is not present on the database
     */
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
        log.info("[SERVICE] set rider "+riderId+" into the order with id "+order);

    }

    /**
     * It retrieves from the database all the orders of a given rider, having 'accepted', 'shipped' or
     * 'completed' as state.
     * @param riderId is the rider ID
     * @return List<OrderEntity>
     */
    @Override
    public List<OrderEntity> getAtLeastAcceptedOrdersByRider(Long riderId) { //accettati-shipped-completed
        log.info("[SERVICE] get accepted/shipped/completed order for rider: "+riderId);
        return orderRepository.findAllByRiderIdAndOrderStateIn(riderId, List.of(ACCEPTED, SHIPPED, COMPLETED));
    }

    /**
     * It defines a thread that 10 minutes before the delivery of the order checks if that order has not yet been accepted:
     * in this case the order is automatically refused.
     */
    @Scheduled(fixedDelay = 10000)
    public void refuseExpiredOrders(){
        log.info("Thread to delete expired order started");
        List <OrderEntity> list = orderRepository.findPendingAndSemiaccptedOrders();

        if(list.isEmpty()){
            log.info("No order is expired");
            return;
        }

        for (OrderEntity order : list) {
            LocalDateTime deliveryTime = order.getDeliveryTime();
            if(deliveryTime.minusMinutes((OrderEntity.minuteOffsetDeliveryTime)).isBefore(LocalDateTime.now())) {
                log.info("Order: " + order.getId() + " Expired");
                order.setOrderState(REFUSED);
                orderRepository.save(order);
                log.info("Order: " + order.getId() + " Refused ");
            }
        }
    }


    }


