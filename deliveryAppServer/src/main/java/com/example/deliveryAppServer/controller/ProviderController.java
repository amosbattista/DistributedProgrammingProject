package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.exception.OrderNotFound;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.dao.order.DishEntity;
import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.service.OrderService;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
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


    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public void login(@RequestParam(name = "availability") Boolean avail, @RequestParam(name = "id") Long id){
        providerService.setAvailability(avail,id);

        log.info("[REST Controller] Login provider");
    }


    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login provider");
        String username = params.get("username");
        String password = params.get("password");
        return providerService.login(username, password);
    }

    @GetMapping("/getPendingOrders/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getPendingOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Pending Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.PENDING);
    }

    @GetMapping("/getAcceptedOrders/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getAcceptedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Accepted Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.ACCEPTED);
    }

    @GetMapping("/getSemiAcceptedOrders/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getSemiAcceptedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get SemiAccepted Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.SEMI_ACCEPTED);
    }

    @GetMapping("/getShippedOrders/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getShippedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Shipped Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.SHIPPED);
    }

    @GetMapping("/getCompletedOrders/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderEntity> getCompletedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Completed Orders");
        return orderService.getProviderOrdersByState(providerId, OrderState.COMPLETED);
    }

    @PutMapping("/putTakeAwayOrder") //PER TAKE-AWAY o SPEDIZIONE con i miei fattorini
    @ResponseStatus(code = HttpStatus.OK)
    public void acceptTakeAwayOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.TAKE_AWAY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
    }

    @PutMapping("/putNoRiderDeliveringOrder") //SPEDIZIONE con i miei fattorini
    @ResponseStatus(code = HttpStatus.OK)
    public void acceptNoRiderOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }

        orderService.changeOrderType(orderId, OrderType.DELIVERY_NORIDER);
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
    }


    @PutMapping("/putRiderOrder") //SOLO PER RIDERS
    @ResponseStatus(code = HttpStatus.OK)
    public void semiAcceptedOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }

        orderService.changeOrderType(orderId, OrderType.DELIVERY_RIDERS);
        orderService.changeOrderState(orderId, OrderState.SEMI_ACCEPTED);
    }

    @PutMapping("/refuseTakeAway")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseTaWOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put refuse order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.TAKE_AWAY)){
            throw new OrderNotFound("Wrong order type!");
        }

        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    @PutMapping("/refuseNoRider")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseNoRiderOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put refuse order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderType(orderId, OrderType.DELIVERY_NORIDER);
        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    @PutMapping("/refuseRider")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseRiderOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put refuse order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderType(orderId, OrderType.DELIVERY_RIDERS);
        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    @PutMapping("/putCompletedHandOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void completeHandOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put completed order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.TAKE_AWAY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }

    @PutMapping("/putShipOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void putOnShippedOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY_NORIDER)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.SHIPPED);
    }

    @PutMapping("/putCompletedOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void putCompletedOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrderState(orderId).getOrderType().equals(OrderType.DELIVERY_NORIDER)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }



    @PostMapping("/postMenu")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postNewOrder(@Valid @RequestBody MenuEntity menu){
        log.info("[REST Controller] Post menu");
        providerService.createNewMenu(menu);


    }

    @GetMapping("/getMenu/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MenuEntity getMenu(@PathVariable("provider-id") Long providerId){
        log.info("[REST Controller] Get Menu for provider: "+providerId);
        return providerService.getMenu(providerId);
    }

    @PostMapping("/addDish/{provider-id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DishEntity addDish(@PathVariable("provider-id") Long providerId, @Valid @RequestBody DishEntity dish){
        log.info("[REST Controller] Add Dish for provider: "+providerId);
        return providerService.addDish(dish, providerId);
    }



}
