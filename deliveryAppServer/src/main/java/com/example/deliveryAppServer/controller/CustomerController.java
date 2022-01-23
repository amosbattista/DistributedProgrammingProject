package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

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


}
