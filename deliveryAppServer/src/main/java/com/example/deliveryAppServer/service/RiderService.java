package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.RiderEntity;

public interface RiderService extends PersonService{

    public void createNewRider(RiderEntity rider);
    public void updateRider(RiderEntity rider);

}
