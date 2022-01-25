package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.InvalidCredentials;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.user.PersonEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import com.example.deliveryAppServer.repository.PersonRepository;
import com.example.deliveryAppServer.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Override
    public Long login(String username, String password) {


        UserEntity personEntity;

        try{
            personEntity = (PersonEntity) personRepository.findByUsername(username);
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        if (!personEntity.getPassword().equals(password)) {
            throw new InvalidCredentials();
        }

        return personEntity.getId();

    }

    @Override
    public void updateBalance(Double value, Long id) {

        PersonEntity user;

        try{
            user = (PersonEntity) personRepository.findById(id).get();
        }
        catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        user.updateBalance(value);
        personRepository.save(user);


    }


}
