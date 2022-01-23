package com.example.deliveryAppServer.controller;

import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.RiderEntity;
import com.example.deliveryAppServer.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rider")
@Slf4j
public class RiderController {

    @Autowired
    private RiderService riderService;

    //@Autowired
    //private OrderService orderService;

    @PostMapping("/postRider")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNewRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Post new rider");
        riderService.createNewRider(rider);
    }

    @PutMapping("/putRider")
    @ResponseStatus(code = HttpStatus.OK)
    public void putRider(@Valid @RequestBody RiderEntity rider){
        log.info("[REST Controller] Put rider");
        riderService.updateRider(rider);
    }


}
