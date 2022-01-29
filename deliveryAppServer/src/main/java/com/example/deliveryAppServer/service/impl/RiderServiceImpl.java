package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import com.example.deliveryAppServer.repository.RiderRepository;
import com.example.deliveryAppServer.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RiderServiceImpl extends PersonServiceImpl implements RiderService {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    public RiderServiceImpl(RiderRepository riderRepository){
        personRepository = riderRepository;
    }

    @Override
    public RiderEntity createNewRider(RiderEntity rider){

        if(riderRepository.existsByUsername(rider.getUsername())){
            throw new UserAlreadyExists("Rider " + rider.getUsername() + " already exists!");   //NON FUNZIONA CON ALTRE ENTITA'
        }

        if(riderRepository.existsByTelephoneNumber(rider.getTelephoneNumber())){
            throw new UserAlreadyExists("Rider tel. number " + rider.getTelephoneNumber() + " already exists!"); //NON FUNZIONA CON ALTRE ENTITA'
        }

        rider = riderRepository.save(rider);
        log.info("[SERVICE] New rider " + rider.getUsername() + " created!");
        return rider;
    }

    @Override
    public RiderEntity updateRider(RiderEntity newRider){

        if(newRider.getId()==null || !riderRepository.existsById(newRider.getId())){
            throw new UserNotFound();
        }

        RiderEntity prevRider = riderRepository.getById(newRider.getId());

        if(riderRepository.existsByUsernameExceptMyself(newRider.getId(), newRider.getUsername())){
            throw new UserAlreadyExists("Username " +newRider.getUsername()+" not available");
        }

        if(riderRepository.existsByTelephoneNumberExceptMyself(newRider.getId(), newRider.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+newRider.getTelephoneNumber()+" not available");
        }


        newRider.setBalance(prevRider.getBalance());

        if(newRider.getPassword()==null || newRider.getPassword().isBlank())
            newRider.setPassword(prevRider.getPassword());


        newRider = riderRepository.save(newRider);
        log.info("[SERVICE]"+newRider.getUsername()+" successfully updated!");
        return newRider;
    }


}
