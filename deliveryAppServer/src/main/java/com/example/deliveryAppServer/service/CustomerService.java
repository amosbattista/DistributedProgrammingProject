package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;

public interface CustomerService extends PersonService {

    public void createNewCustomer(CustomerEntity customer) throws UserAlreadyExists;
    public void updateCustomer(CustomerEntity customer);
}
