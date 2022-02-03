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

    @GetMapping("/avail-providers")
    public List<ProviderDto> getAvailableProvidersDTO(){
        log.info("[REST Controller] Get available providers");
        List<ProviderEntity> prov = providerService.getAvailableProviders();

        return modelMapper.convertProviderListToDto(prov);
    }

    @GetMapping("/{customer-id}/myorders")
    public List<OrderDto> getOrderHistory(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get order history for id: "+customerId);
        List<OrderEntity> orders = orderService.getCustomerOrdersHistory(customerId);
        return modelMapper.convertOrderListToDto(orders);

    }

    @GetMapping("/{customer-id}/current-order")
    public OrderDto getCurrentOrderDTO(@PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Get current order for customer: "+customerId);
        OrderEntity order = orderService.getCurrentOrder(customerId);
        return modelMapper.convertOrderToDtoForCustomer(order);
    }

    @GetMapping("/order/{order-id}") // serve??
    public OrderDto getOrderStateDTO(@PathVariable("order-id") Long orderId){
        log.info("[REST Controller] Get order state, id=: "+orderId);
        OrderEntity order = orderService.getOrderState(orderId);
        return modelMapper.convertOrderToDtoForCustomer(order);
    }

    @PostMapping("/postCustomer")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CustomerEntity createNewCustomer(@Valid @RequestBody CustomerEntity customer){
        log.info("[REST Controller] Post new customer");
        return customerService.createNewCustomer(customer);
    }

    @PutMapping("/putCustomer")
    @ResponseStatus(code = HttpStatus.OK)
    public CustomerEntity putCustomer(@Valid @RequestBody CustomerEntity customer){
        log.info("[REST Controller] Put customer");
        return customerService.updateCustomer(customer);
    }

    @PostMapping("/postOrder")
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderDto postNewOrder(@Valid @RequestBody OrderEntity order){
        log.info("[REST Controller] Post order");
        OrderEntity newOrder = orderService.createNewOrder(order);
        return modelMapper.convertOrderToDtoForCustomer(newOrder);

    }

    @PostMapping("/postOrderDto")
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderDto postNewOrderDto(@Valid @RequestBody OrderDto orderDto){
        log.info("[REST Controller] Post order");
        OrderEntity orderDao = modelMapper.convertOrderDtoToDao(orderDto);
        OrderEntity newOrder = orderService.createNewOrder(orderDao);
        return modelMapper.convertOrderToDtoForCustomer(newOrder);

    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Long login(@RequestBody Map<String, String> params){
        log.info("[REST Controller] Login CUSTOMER");
        String username = params.get("username");
        String password = params.get("password");
        return customerService.login(username, password);
    }

    @PutMapping("/{customer-id}/balance")
    @ResponseStatus(code = HttpStatus.OK)
    public void increaseBalance(@RequestParam(name = "value") Double increment, @PathVariable("customer-id") Long customerId){
        log.info("[REST Controller] Increase Customer Balance");
        customerService.updateBalance(increment, customerId);

    }


}
