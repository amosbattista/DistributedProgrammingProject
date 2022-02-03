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
public class RiderController {

    @Autowired
    private RiderService riderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapperDto modelMapper;


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

    @PostMapping("/postRider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public RiderEntity createNewRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Post new rider");
        return riderService.createNewRider(rider);
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login rider");
        String username = params.get("username");
        String password = params.get("password");
        return riderService.login(username, password);
    }

    @PutMapping("/updateRider")
    @ResponseStatus(code = HttpStatus.OK)
    public RiderEntity updateRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Put rider");
        return riderService.updateRider(rider);
    }

    @GetMapping("/getSemiAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getSemiAcceptedOrders(){
        log.info("[REST Controller] Get semi accepted orders");
        List<OrderEntity> orderEntityList = orderService.getOrdersByState(OrderState.SEMI_ACCEPTED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }


    @GetMapping("/{rider-id}/getAtLeastAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getAtLeastAcceptedOrders(@PathVariable(name = "rider-id") Long riderId){
        log.info("[REST Controller] Get accepted orders");
        List<OrderEntity> orderEntityList = orderService.getAtLeastAcceptedOrdersByRider(riderId);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    @PutMapping("/{rider-id}/acceptOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void acceptOrder(@RequestParam(name = "id") Long orderId, @PathVariable("rider-id") Long riderId){
        log.info("[REST Controller] Accept order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY_RIDERS)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
        orderService.setRiderOrder(orderId, riderId);
    }

    @PutMapping("/shipOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void shipOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Ship order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY_RIDERS)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.SHIPPED);
    }

    @PutMapping("/deliveredOrder")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deliveredOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Delivered order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY_RIDERS)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }

    @GetMapping("/order/{order-id}") // serve??
    public OrderDto getOrderDtoById(@PathVariable("order-id") Long orderId){
        log.info("[REST Controller] Get order state, id=: "+orderId);
        OrderEntity order = orderService.getOrderState(orderId);
        return modelMapper.convertOrderToDtoForCustomer(order);
    }





}
