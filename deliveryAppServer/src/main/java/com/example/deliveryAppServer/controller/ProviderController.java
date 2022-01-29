package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.exception.OrderNotFound;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.mapper.ModelMapperDto;
import com.example.deliveryAppServer.model.dto.order.OrderDto;
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

    @Autowired
    private ModelMapperDto modelMapper;

    @GetMapping("/{provider-id}/myinfo")
    @ResponseStatus(code = HttpStatus.OK)
    public ProviderEntity getMyInfo(@PathVariable("provider-id") Long providerId){
        log.info("[REST Controller] Get provider info for id: "+providerId);

            try{
               return (ProviderEntity) providerService.getPerson(providerId);
                }
            catch (ClassCastException exception){
                throw new UserNotFound();
             }

    }


    @PostMapping("/postProvider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProviderEntity createNewProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Post new provider");
        return providerService.createNewProvider(provider);
    }

    @PutMapping("/putProvider")
    @ResponseStatus(code = HttpStatus.OK)
    public ProviderEntity putProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Put provider");
        return providerService.updateProvider(provider);
    }

    @GetMapping("/getAvalaibleProviders") // serve??
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProviderEntity> getAvailaibleProvider(){
        log.info("[REST Controller] Get Available provider");
        return providerService.getAvailableProviders();
    }


    @PostMapping("/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void setAvail(@RequestParam(name = "availability") Boolean avail, @PathVariable("provider-id") Long providerId){
        providerService.setAvailability(avail,providerId);

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

    @GetMapping("/{provider-id}/getPendingOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getPendingOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Pending Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.PENDING);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    @GetMapping("/{provider-id}/getAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getAcceptedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Accepted Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.ACCEPTED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    @GetMapping("/{provider-id}/getSemiAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getSemiAcceptedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get SemiAccepted Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.SEMI_ACCEPTED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    @GetMapping("/{provider-id}/getShippedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getShippedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Shipped Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.SHIPPED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    @GetMapping("/{provider-id}/getCompletedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getCompletedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Completed Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.COMPLETED);
        return modelMapper.convertOrderListToDto(orderEntityList);

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
    public void postNewMenu(@Valid @RequestBody MenuEntity menu){
        log.info("[REST Controller] Post menu");
        providerService.createNewMenu(menu);


    }

    @GetMapping("/{provider-id}/getMenu")
    @ResponseStatus(code = HttpStatus.OK)
    public MenuEntity getMenu(@PathVariable("provider-id") Long providerId){
        log.info("[REST Controller] Get Menu for provider: "+providerId);
        return providerService.getMenu(providerId);
    }

    @PostMapping("/{provider-id}/addDish")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DishEntity addDish(@PathVariable("provider-id") Long providerId, @Valid @RequestBody DishEntity dish){
        log.info("[REST Controller] Add Dish for provider: "+providerId);
        return providerService.addDish(dish, providerId);
    }

    @DeleteMapping("/{provider-id}/removeDish")
    @ResponseStatus(code = HttpStatus.OK)
    public void removeDish(@PathVariable("provider-id") Long providerId, @RequestParam(name = "dish_id") Long dishId){

        log.info("[REST Controller] Remove Dish for provider: "+providerId);
        providerService.removeDish(dishId, providerId);
    }



}
