package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.model.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.order.MenuEntity;
import com.example.deliveryAppServer.model.order.OrderEntity;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.example.deliveryAppServer.service.CustomerService;
import com.example.deliveryAppServer.service.OrderService;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.example.deliveryAppServer.model.enumerations.OrderState.COMPLETED;
import static com.example.deliveryAppServer.model.enumerations.OrderState.REFUSED;

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



    @GetMapping("/avail-providers")
    public List<ProviderEntity> getAvailableProviders(){
        log.info("[REST Controller] Get available providers");
        List<ProviderEntity> prov = providerService.getAvailableProviders();
        return prov;
    }

    @GetMapping("/myorders/{customer-id}")
    public List<OrderEntity> getOrderHistory(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get order history for id: "+customerId);
        List<OrderEntity> orders = orderService.getCustomerOrdersHistory(customerId);
        return orders;

    }

    @GetMapping("/current-order/{customer-id}")
    public OrderEntity getCurrentOrder(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get current order for customer: "+customerId);
        OrderEntity order = orderService.getCurrentOrder(customerId);
        return order;
    }

    @GetMapping("/order/{order-id}")
    public OrderEntity getOrderState(@PathVariable("order-id") Long orderId){
        log.info("[REST Controller] Get order state, id=: "+orderId);
        OrderEntity order = orderService.getOrderState(orderId);
        return order;
    }

    @PostMapping("/postCustomer")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNewCustomer(@Valid @RequestBody CustomerEntity customer){
        log.info("[REST Controller] Post new customer");
        customerService.createNewCustomer(customer);
    }

    @PutMapping("/putCustomer")
    @ResponseStatus(code = HttpStatus.OK)
    public void putCustomer(@Valid @RequestBody CustomerEntity customer){
        log.info("[REST Controller] Put customer");
        customerService.updateCustomer(customer);
    }

    @PostMapping("/postOrder")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postNewOrder(@Valid @RequestBody  OrderEntity order){
        log.info("[REST Controller] Post order");
        orderService.createNewOrder(order);


    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login CUSTOMER");
        String username = params.get("username");
        String password = params.get("password");
        return customerService.login(username, password);
    }

    @PutMapping("/balance")
    @ResponseStatus(code = HttpStatus.OK)
    public void increaseBalance(@RequestParam(name = "value") Double increment, @RequestParam(name = "id") Long id){
        log.info("[REST Controller] Increase Customer Balance");
        customerService.updateBalance(increment, id);

    }





}
