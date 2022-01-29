package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;

public interface CustomerService extends PersonService {

    public CustomerEntity createNewCustomer(CustomerEntity customer) throws UserAlreadyExists;
    public CustomerEntity updateCustomer(CustomerEntity customer);



}
