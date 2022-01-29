package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.dao.user.RiderEntity;

public interface RiderService extends PersonService{

    public RiderEntity createNewRider(RiderEntity rider);
    public RiderEntity updateRider(RiderEntity rider);

}
