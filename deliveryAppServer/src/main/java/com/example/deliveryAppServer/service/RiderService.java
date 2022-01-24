package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.RiderEntity;

public interface RiderService {

    public void createNewRider(RiderEntity rider);
    public Long loginRider(String username, String password);
    public void updateRider(RiderEntity rider);

}
