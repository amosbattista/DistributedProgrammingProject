package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.model.user.CustomerEntity;

public interface CustomerService {

    public void createNewCustomer(CustomerEntity customer) throws UserAlreadyExists;
    public void updateCustomer(CustomerEntity customer);
    public Long loginCustomer (String username, String password);

}
