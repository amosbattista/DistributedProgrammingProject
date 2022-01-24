package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.InvalidCredentials;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.PersonEntity;
import com.example.deliveryAppServer.model.user.RiderEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import com.example.deliveryAppServer.repository.UserRepository;
import com.example.deliveryAppServer.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public Long login(String username, String password, UserRepository repository) {


        UserEntity userEntity;

        try{
            userEntity = repository.findByUsername(username);
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        if (!userEntity.getPassword().equals(password)) {
            throw new InvalidCredentials();
        }

        return userEntity.getId();

    }

    @Override
    public void updateBalance(Double value, Long id, UserRepository repository) {

        PersonEntity user;

        try{
            user = (PersonEntity) repository.findById(id).get();
        }
        catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        user.updateBalance(value);
        repository.save(user);


    }


}
