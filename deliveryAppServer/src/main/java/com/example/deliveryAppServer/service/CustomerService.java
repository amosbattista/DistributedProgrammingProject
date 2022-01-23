package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.model.user.CustomerEntity;

public interface CustomerService {

    public void createNewCustomer(CustomerEntity customer) throws UserAlreadyExists;
    public void updateCustomer(CustomerEntity customer);
    public void decreaseBalance(double value);
    public void encreaseBalance(double value);

    public Long login(String username, String password);

}
