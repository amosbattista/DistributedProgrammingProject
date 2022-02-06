package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.exception.OrderNotFound;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.mapper.ModelMapperDto;
import com.example.deliveryAppServer.model.dto.order.OrderDto;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import com.example.deliveryAppServer.service.OrderService;
import com.example.deliveryAppServer.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rider")
@Slf4j
/**
 * It defines and builds all the API calls for the rider user, using the public methods of server services (customerServices,
 * providerService and orderService). It also uses a mapper to convert dao to dto entities.
 */
public class RiderController {

    @Autowired
    private RiderService riderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapperDto modelMapper;


    /**
     * [GET] It retrieves from the server the Rider passed through ID
     * @param riderId  is the ID of the rider to retrieve
     * @return a RiderEntity expressed as JSON
     * @throws UserNotFound if there is no rider with the given ID
     */
    @GetMapping("/{rider-id}/myinfo")
    public RiderEntity getMyInfo(@PathVariable("rider-id") Long riderId){
        log.info("[REST Controller] Get rider info for id: "+riderId);

        try{
            return (RiderEntity) riderService.getPerson(riderId);
        }
        catch (ClassCastException exception){
            throw new UserNotFound();
        }

    }

    /**
     * [POST] It creates a new rider in the server
     * @param rider represent the Rider to be created
     * @return RiderEntity just created, expressed as a JSON
     */
    @PostMapping("/postRider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public RiderEntity createNewRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Post new rider");
        return riderService.createNewRider(rider);
    }

    /**
     * [POST] It allows the login to a rider, given his credentials
     * @param params is a Map in which is required to insert username and password
     * @return a Long that represents the rider ID
     */
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login rider");
        String username = params.get("username");
        String password = params.get("password");
        return riderService.login(username, password);
    }

    /**
     * [PUT] It updates into the server the RiderEntity passed;
     * @param rider represent the rider to be updated
     * @return RiderEntity just updated, expressed as a JSON
     */
    @PutMapping("/updateRider")
    @ResponseStatus(code = HttpStatus.OK)
    public RiderEntity updateRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Put rider");
        return riderService.updateRider(rider);
    }

    /**
     * [GET] It retrieves from the server an order identified by ID; convert the OrderEntity in
     * a OrderDto through a model mapper
     * @param orderId is the order ID
     * @return OrderDto expressed as a JSON
     */
    @GetMapping("/order/{order-id}")
    public OrderDto getOrderDtoById(@PathVariable("order-id") Long orderId){
        log.info("[REST Controller] Get order state, id=: "+orderId);
        OrderEntity order = orderService.getOrder(orderId);
        return modelMapper.convertOrderToDto(order);
    }

    /**
     * [GET] It retrieves from the server the whole partial-accepted orders list; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/getSemiAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getSemiAcceptedOrders(){
        log.info("[REST Controller] Get semi accepted orders");
        List<OrderEntity> orderEntityList = orderService.getOrdersByState(OrderState.SEMI_ACCEPTED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }


    /**
     * [GET] It retrieves from the server a list of orders that have been accepted by the rider, identified by ID;
     * convert the List<OrderEntity> in a List<OrderDto> through a model mapper.
     * @param riderId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{rider-id}/getAtLeastAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getAtLeastAcceptedOrders(@PathVariable(name = "rider-id") Long riderId){
        log.info("[REST Controller] Get accepted orders");
        List<OrderEntity> orderEntityList = orderService.getAtLeastAcceptedOrdersByRider(riderId);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'accepted'.
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/{rider-id}/acceptOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void acceptOrder(@RequestParam(name = "id") Long orderId, @PathVariable("rider-id") Long riderId){
        log.info("[REST Controller] Accept order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY_RIDERS)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
        orderService.setRiderOrder(orderId, riderId);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'shipped'.
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/shipOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void shipOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Ship order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY_RIDERS)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.SHIPPED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'completed'.
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/deliveredOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deliveredOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Delivered order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY_RIDERS)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }


}
