package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.order.OrderEntity;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.example.deliveryAppServer.service.OrderService;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@Slf4j
public class ProviderController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private ProviderService providerService;

    @PostMapping("/postProvider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNewProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Post new provider");
        providerService.createNewProvider(provider);
    }

    @PutMapping("/putProvider")
    @ResponseStatus(code = HttpStatus.OK)
    public void putProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Put provider");
        providerService.updateProvider(provider);
    }
    @GetMapping("/getAvalaibleProviders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProviderEntity> getAvailaibleProvider(){
        log.info("[REST Controller] Get Available provider");
        return providerService.getAvailableProviders();
    }



    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login provider");
        String username = params.get("username");
        String password = params.get("password");
        return providerService.loginProvider(username, password);
    }

    @GetMapping("/getPendingOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getPendingOrders(Long providerId){
        log.info("[REST Controller] Get Pending Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.PENDING);
    }

    @GetMapping("/getAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getAcceptedOrders(Long providerId){
        log.info("[REST Controller] Get Accepted Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.ACCEPTED);
    }

    @GetMapping("/getSemiAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getSemiAcceptedOrders(Long providerId){
        log.info("[REST Controller] Get SemiAccepted Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.SEMI_ACCEPTED);
    }

    @GetMapping("/getShippedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getShippedOrders(Long providerId){
        log.info("[REST Controller] Get Shipped Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.SHIPPED);
    }

    @GetMapping("/getCompletedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getCompletedOrders(Long providerId){
        log.info("[REST Controller] Get Completed Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.COMPLETED);
    }

    @PutMapping("/putAcceptOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void acceptOrder(@RequestBody Long orderId){
        log.info("[REST Controller] Put accept order");
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
    }

    @PutMapping("/putRefuseOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseOrder(@RequestBody Long orderId){
        log.info("[REST Controller] Put refuse order");
        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    @PutMapping("/putHandOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void handOrder(@RequestBody Long orderId){
        log.info("[REST Controller] Put completed order");
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }

    @PutMapping("/putShipOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void shipOrder(@RequestBody Long orderId){
        log.info("[REST Controller] Put accept order");
        orderService.changeOrderState(orderId, OrderState.SHIPPED);
    }

    @PutMapping("/SemiAcceptedOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void semiAcceptedOrder(@RequestBody Long orderId){
        log.info("[REST Controller] Put accept order");
        orderService.changeOrderState(orderId, OrderState.SEMI_ACCEPTED);
    }



}
