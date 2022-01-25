package com.example.deliveryAppServer.service;

public interface PersonService {

    public Long login(String username, String password);
    public void updateBalance(Double value, Long id);
}
