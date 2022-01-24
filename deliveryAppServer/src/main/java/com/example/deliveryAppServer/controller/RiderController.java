package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.order.OrderEntity;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.RiderEntity;
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
public class RiderController {

    @Autowired
    private RiderService riderService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/postRider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNewRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Post new rider");
        riderService.createNewRider(rider);
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login rider");
        String username = params.get("username");
        String password = params.get("password");
        return riderService.loginRider(username, password);
    }

    @PutMapping("/updateRider")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Put rider");
        riderService.updateRider(rider);
    }

    @GetMapping("/getSemiAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getSemiAcceptedOrders(){
        log.info("[REST Controller] Get semi accepted orders");
        return orderService.getOrdersByState(OrderState.SEMI_ACCEPTED);
    }

    @PutMapping("/acceptOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void acceptOrder(long orderId){
        log.info("[REST Controller] Accept order");
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
    }

    @PutMapping("/shipOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void shipOrder(long orderId){
        log.info("[REST Controller] Ship order");
        orderService.changeOrderState(orderId, OrderState.SHIPPED);
    }

    @PutMapping("/deliveredOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deliveredOrder(long orderId){
        log.info("[REST Controller] Delivered order");
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }





}
