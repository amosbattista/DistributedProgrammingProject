package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.mapper.ModelMapperDto;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dto.order.OrderDto;
import com.example.deliveryAppServer.model.dto.user.ProviderDto;
import com.example.deliveryAppServer.service.CustomerService;
import com.example.deliveryAppServer.service.OrderService;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * It defines and builds all the API calls for the customer user, using the public methods of server services (customerServices,
 * providerService and orderService). It also uses a mapper to convert dao to dto entities.
 */
@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapperDto modelMapper;


    /**
     * [GET] It retrieves from the server the Customer passed through ID
     * @param customerId is the ID of the customer to retrieve
     * @return a CustomerEntity expressed as JSON
     * @throws UserNotFound if there is no customer with the given ID
     */
    @GetMapping("/{customer-id}/myinfo")
    public CustomerEntity getMyInfo(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get customer info for id: "+customerId);

        try{
            return (CustomerEntity) customerService.getPerson(customerId);
        }
        catch (ClassCastException exception){
            throw new UserNotFound();
        }

    }

    /**
     * [GET] It retrieves form the server a list of available ProviderEntity; convert the List<ProviderEntity> in a
     * List<ProviderDto> through a model mapper.
     * @return List<ProviderDto> expressed as JSON list
     */
    @GetMapping("/avail-providers")
    public List<ProviderDto> getAvailableProvidersDTO(){
        log.info("[REST Controller] Get available providers");
        List<ProviderEntity> prov = providerService.getAvailableProviders();
        return modelMapper.convertProviderListToDto(prov);
    }

    /**
     * [GET] It retrieves from the server the orders list of a given Customer identified by ID; convert the List<OrderEntity> in a
     * List<OrderDto> through a model mapper.
     * @param customerId is the customer ID
     * @return List<OrderDto> expressed as JSON list
     */
    @GetMapping("/{customer-id}/myorders")
    public List<OrderDto> getOrderHistory(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get order history for id: "+customerId);
        List<OrderEntity> orders = orderService.getCustomerOrdersHistory(customerId);
        return modelMapper.convertOrderListToDto(orders);

    }

    /**
     * [GET] It retrieves from the server the current order of a Customer identified by ID; convert the OrderEntity in
     * a OrderDto through a model mapper
     * @param customerId is the customer ID
     * @return OrderDto expressed as a JSON
     */
    @GetMapping("/{customer-id}/current-order")
    public OrderDto getCurrentOrderDTO(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get current order for customer: "+customerId);
        OrderEntity order = orderService.getCurrentOrder(customerId);
        return modelMapper.convertOrderToDto(order);
    }

    /**
     * [GET] It retrieves from the server an order identified by ID; convert the OrderEntity in
     * a OrderDto through a model mapper
     * @param orderId is the order ID
     * @return OrderDto expressed as a JSON
     */
    @GetMapping("/order/{order-id}")
    public OrderDto getOrderStateDTO(@PathVariable("order-id") Long orderId){
        log.info("[REST Controller] Get order state, id=: "+orderId);
        OrderEntity order = orderService.getOrder(orderId);
        return modelMapper.convertOrderToDto(order);
    }

    /**
     * [POST] It creates a new customer in the server
     * @param customer represent the Customer to be created
     * @return CustomerEntity just created, expressed as a JSON
     */
    @PostMapping("/postCustomer")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CustomerEntity createNewCustomer(@Valid @RequestBody CustomerEntity customer){
        log.info("[REST Controller] Post new customer");
        return customerService.createNewCustomer(customer);
    }

    /**
     * [PUT] It updates into the server the CustomerEntity passed;
     * @param customer represent the Customer to be updated
     * @return CustomerEntity just updated, expressed as a JSON
     */
    @PutMapping("/putCustomer")
    @ResponseStatus(code = HttpStatus.OK)
    public CustomerEntity putCustomer(@Valid @RequestBody CustomerEntity customer){
        log.info("[REST Controller] Put customer");
        return customerService.updateCustomer(customer);
    }

    @PostMapping("/postOrder") //da eliminare
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderDto postNewOrder(@Valid @RequestBody OrderEntity order){
        log.info("[REST Controller] Post order");
        OrderEntity newOrder = orderService.createNewOrder(order);
        return modelMapper.convertOrderToDto(newOrder);

    }

    /**
     * [POST] It creates a new order in the server; convert the OrderDto in OrderEntity through a model mapper
     * @param orderDto represent the Order to be created
     * @return the OrderDto just created
     */
    @PostMapping("/postOrderDto")
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderDto postNewOrderDto(@Valid @RequestBody OrderDto orderDto){
        log.info("[REST Controller] Post order");
        OrderEntity orderDao = modelMapper.convertOrderDtoToDao(orderDto);
        OrderEntity newOrder = orderService.createNewOrder(orderDao);
        return modelMapper.convertOrderToDto(newOrder);

    }

    /**
     * [POST] It allows the login to a customer, given his credentials
     * @param params is a Map in which is required to insert username and password
     * @return a Long that represents the customer ID
     */
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login CUSTOMER");
        String username = params.get("username");
        String password = params.get("password");
        return customerService.login(username, password);
    }

    /**
     * [PUT] It increases the customer balance, given an increment (can be also negative)
     * @param increment is a Double that represents the amount of money to be added to the balance
     * @param customerId is a Long that represents the customer ID
     */
    @PutMapping("/{customer-id}/balance")
    @ResponseStatus(code = HttpStatus.OK)
    public void increaseBalance(@RequestParam(name = "value") Double increment, @PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Increase Customer Balance");
        customerService.updateBalance(increment, customerId);

    }


}
