package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.RiderEntity;

public interface RiderService {

    public void createNewRider(RiderEntity rider);
    public void login(String username, String password);
    public void updateRider(RiderEntity rider);

    public void decreaseBalance(double value);
    public void increaseBalance(double value);
}
