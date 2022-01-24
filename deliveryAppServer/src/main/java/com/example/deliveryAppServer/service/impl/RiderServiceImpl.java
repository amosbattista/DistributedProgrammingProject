package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.InsufficientBalanceException;
import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.user.RiderEntity;
import com.example.deliveryAppServer.repository.RiderRepository;
import com.example.deliveryAppServer.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderRepository riderRepository;

    @Override
    public void createNewRider(RiderEntity rider){

        if(riderRepository.existsByUsername(rider.getUsername())){
            throw new UserAlreadyExists("Rider " + rider.getUsername() + " already exists!");
        }

        if(riderRepository.existsByTelephoneNumber(rider.getTelephoneNumber())){
            throw new UserAlreadyExists("Rider tel. number " + rider.getTelephoneNumber() + " already exists!");
        }

        riderRepository.save(rider);
        log.info("[SERVICE] New rider " + rider.getUsername() + " created!");
    }

    @Override
    public void login(String username, String password){}

    @Override
    public void updateRider(RiderEntity rider){

        if(!riderRepository.existsById(rider.getId())){
            throw new UserNotFound();
        }

        if(riderRepository.existsByUsernameExceptMyself(rider.getId(), rider.getUsername())){
            throw new UserAlreadyExists("Username " +rider.getUsername()+" not available");
        }

        if(riderRepository.existsByTelephoneNumberExceptMyself(rider.getId(), rider.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+rider.getTelephoneNumber()+" not available");
        }

        riderRepository.save(rider);
        log.info("[SERVICE]"+rider.getUsername()+" successfully updated!");
    }

    @Override
    public void decreaseBalance(long id, double value){

        RiderEntity riderEntity;

        try{
            riderEntity = riderRepository.findById(id).get();
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        if(riderEntity == null){
            throw new UserNotFound();
        }

        double currentBalance = riderEntity.getBalance();
        double newBalance = currentBalance - value;
        if(newBalance < 0)
            throw new InsufficientBalanceException();
        else {
            riderEntity.setBalance(currentBalance - value);
            riderRepository.save(riderEntity);
        }

    }

    @Override
    public void increaseBalance(long id, double value){

        RiderEntity riderEntity;

        try{
            riderEntity = riderRepository.findById(id).get();
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        double currentBalance = riderEntity.getBalance();
        riderEntity.setBalance(currentBalance + value);

        riderRepository.save(riderEntity);

    }
}
