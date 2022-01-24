package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.InsufficientBalanceException;
import com.example.deliveryAppServer.exception.InvalidCredentials;
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
public class RiderServiceImpl extends UserServiceImpl implements RiderService {

    @Autowired
    private RiderRepository riderRepository;

    @Override
    public void createNewRider(RiderEntity rider){

        if(riderRepository.existsByUsername(rider.getUsername())){
            throw new UserAlreadyExists("Rider " + rider.getUsername() + " already exists!");   //NON FUNZIONA CON ALTRE ENTITA'
        }

        if(riderRepository.existsByTelephoneNumber(rider.getTelephoneNumber())){
            throw new UserAlreadyExists("Rider tel. number " + rider.getTelephoneNumber() + " already exists!"); //NON FUNZIONA CON ALTRE ENTITA'
        }

        riderRepository.save(rider);
        log.info("[SERVICE] New rider " + rider.getUsername() + " created!");
    }


    public Long loginRider(String username, String password){

        return login(username, password, riderRepository);

    }

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

}
