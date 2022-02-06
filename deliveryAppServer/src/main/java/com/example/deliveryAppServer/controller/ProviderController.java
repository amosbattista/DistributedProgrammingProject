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
/**
 * It defines and builds all the API calls for the provider user, using the public methods of server services (customerServices,
 *  * providerService and orderService). It also uses a mapper to convert dao to dto entities.
 */
public class ProviderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ModelMapperDto modelMapper;

    /**
     * [GET] It retrieves from the server the Provider passed through ID
     * @param providerId  is the ID of the provider to retrieve
     * @return a ProviderEntity expressed as JSON
     * @throws UserNotFound if there is no provider with the given ID
     */
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


    /**
     * [POST] It creates a new provider in the server
     * @param provider represent the Provider to be created
     * @return ProviderEntity just created, expressed as a JSON
     */
    @PostMapping("/postProvider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProviderEntity createNewProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Post new provider");
        return providerService.createNewProvider(provider);
    }

    /**
     * [PUT] It updates into the server the ProviderEntity passed;
     * @param provider represent the Provider to be updated
     * @return ProviderEntity just updated, expressed as a JSON
     */
    @PutMapping("/putProvider")
    @ResponseStatus(code = HttpStatus.OK)
    public ProviderEntity putProvider(@Valid @RequestBody ProviderEntity provider){
        log.info("[REST Controller] Put provider");
        return providerService.updateProvider(provider);
    }

    /**
     * [PUT] It updates the provider 'availability' field
     * @param avail is a Boolean that indicates if the provider is available
     * @param providerId is a Long that represents the provider ID
     */
    @PutMapping("/{provider-id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void setAvail(@RequestParam(name = "availability") Boolean avail, @PathVariable("provider-id") Long providerId){
        providerService.setAvailability(avail,providerId);

        log.info("[REST Controller] Login provider");
    }

    /**
     * [POST] It allows the login to a provider, given his credentials
     * @param params is a Map in which is required to insert username and password
     * @return a Long that represents the provider ID
     */
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login provider");
        String username = params.get("username");
        String password = params.get("password");
        return providerService.login(username, password);
    }

    /**
     * [GET] It retrieves from the server the pending orders list of a given Provider identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param providerId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{provider-id}/getPendingOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getPendingOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Pending Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.PENDING);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    /**
     * [GET] It retrieves from the server the accepted orders list of a given Provider identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param providerId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{provider-id}/getAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getAcceptedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Accepted Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.ACCEPTED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    /**
     * [GET] It retrieves from the server the partial-accepted orders list of a given Provider identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param providerId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{provider-id}/getSemiAcceptedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getSemiAcceptedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get SemiAccepted Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.SEMI_ACCEPTED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    /**
     * [GET] It retrieves from the server the shipped orders list of a given Provider identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param providerId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{provider-id}/getShippedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getShippedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Shipped Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.SHIPPED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    /**
     * [GET] It retrieves from the server the completed orders list of a given Provider identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param providerId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{provider-id}/getCompletedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getCompletedOrders(@PathVariable("provider-id")  Long providerId){
        log.info("[REST Controller] Get Completed Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.COMPLETED);
        return modelMapper.convertOrderListToDto(orderEntityList);

    }

    /**
     * [GET] It retrieves from the server the refused orders list of a given Provider identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param providerId is the provider ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("{provider-id}/getRefusedOrders")
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getRefusedOrders(@PathVariable("provider-id") Long providerId){
        log.info("[REST CONTROLLER] Get Refused Orders");
        List<OrderEntity> orderEntityList = orderService.getProviderOrdersByState(providerId, OrderState.REFUSED);
        return modelMapper.convertOrderListToDto(orderEntityList);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'accepted', taking into account that the order type is 'take away'
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/putTakeAwayOrder") //PER TAKE-AWAY
    @ResponseStatus(code = HttpStatus.OK)
    public void acceptTakeAwayOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.TAKE_AWAY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'accepted', taking into account that the order type is
     * a delivery that does not use the riders-app (delivery with your own riders)
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/putNoRiderDeliveringOrder") //SPEDIZIONE con i miei fattorini
    @ResponseStatus(code = HttpStatus.OK)
    public void acceptNoRiderOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }

        orderService.changeOrderType(orderId, OrderType.DELIVERY_NORIDER);
        orderService.changeOrderState(orderId, OrderState.ACCEPTED);
    }


    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'accepted', taking into account that the order type is
     * a delivery that use the riders-app.
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/putRiderOrder") //SOLO PER RIDERS
    @ResponseStatus(code = HttpStatus.OK)
    public void semiAcceptedOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }

        orderService.changeOrderType(orderId, OrderType.DELIVERY_RIDERS);
        orderService.changeOrderState(orderId, OrderState.SEMI_ACCEPTED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'refused', taking into account that the order type is
     * 'take away'
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/refuseTakeAway")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseTaWOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put refuse order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.TAKE_AWAY)){
            throw new OrderNotFound("Wrong order type!");
        }

        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'refused', taking into account that the order type is
     * a delivery that does not use the riders-app (delivery with your own riders)
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/refuseNoRider")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseNoRiderOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put refuse order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderType(orderId, OrderType.DELIVERY_NORIDER);
        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'refused', taking into account that the order type is
     * a delivery that use the riders-app.
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/refuseRider")
    @ResponseStatus(code = HttpStatus.OK)
    public void refuseRiderOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put refuse order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderType(orderId, OrderType.DELIVERY_RIDERS);
        orderService.changeOrderState(orderId, OrderState.REFUSED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'completed', taking into account that the order type is
     * 'take away'
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/putCompletedHandOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void completeHandOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put completed order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.TAKE_AWAY)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'shipped', aking into account that the order type is
     * 'delivery' (regardless of rider type)
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/putShipOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void putOnShippedOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY_NORIDER)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.SHIPPED);
    }

    /**
     * [PUT] It updates the state of the given order (identfied by ID) in 'completed', regardless of order type
     * @param orderId is the order ID
     * @throws OrderNotFound if there is no order with the given ID
     */
    @PutMapping("/putCompletedOrder")
    @ResponseStatus(code = HttpStatus.OK)
    public void putCompletedOrder(@RequestParam(name = "id") Long orderId){
        log.info("[REST Controller] Put accept order");
        if(!orderService.getOrder(orderId).getOrderType().equals(OrderType.DELIVERY_NORIDER)){
            throw new OrderNotFound("Wrong order type!");
        }
        orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }

    /**
     * [POST] It creates a new menu for the provider
     * @param menu represent the Menu to be created
     */
    @PostMapping("/postMenu")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postNewMenu(@Valid @RequestBody MenuEntity menu){
        log.info("[REST Controller] Post menu");
        providerService.createNewMenu(menu);

    }

    /**
     * [GET] It retrieves from the server the menu of the provider identified by his ID
     * @param providerId is the provider ID
     * @return MenuEntity expressed as JSON
     */
    @GetMapping("/{provider-id}/getMenu")
    @ResponseStatus(code = HttpStatus.OK)
    public MenuEntity getMenu(@PathVariable("provider-id") Long providerId){
        log.info("[REST Controller] Get Menu for provider: "+providerId);
        return providerService.getMenu(providerId);
    }

    /**
     * [POST] It adds a new dish to the menu of the provider identified by his ID
     * @param providerId is the provider ID
     * @param dish is the dish entity
     * @return DishEntity just added expressed as JSON
     */
    @PostMapping("/{provider-id}/addDish")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DishEntity addDish(@PathVariable("provider-id") Long providerId, @Valid @RequestBody DishEntity dish){
        log.info("[REST Controller] Add Dish for provider: "+providerId);
        return providerService.addDish(dish, providerId);
    }

    /**
     * [PUT] It updates a dish in the menu of the provider identified by his ID
     * @param providerId is the provider ID
     * @param dish is the dish entity to be updated
     * @return DishEntity just updated expressed as JSON
     */
    @PutMapping("/{provider-id}/updateDish")
    @ResponseStatus(code = HttpStatus.OK)
    public DishEntity updateDish(@PathVariable("provider-id") Long providerId,@Valid @RequestBody DishEntity dish){
        log.info("[REST Controller] Put dish");
        return providerService.updateDish(dish, providerId);

    }

    /**
     * [DELETE] It deletes a dish from the menu of the provider identified by his ID
     * @param providerId is the provider ID
     * @param dishId is the dish entity to be deleted
     */
    @DeleteMapping("/{provider-id}/removeDish")
    @ResponseStatus(code = HttpStatus.OK)
    public void removeDish(@PathVariable("provider-id") Long providerId, @RequestParam(name = "dish_id") Long dishId){

        log.info("[REST Controller] Remove Dish for provider: "+providerId);
        providerService.removeDish(dishId, providerId);
    }




}
