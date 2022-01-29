package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.InvalidCredentials;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.dao.user.PersonEntity;
import com.example.deliveryAppServer.repository.PersonRepository;
import com.example.deliveryAppServer.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PersonServiceImpl <Person extends PersonEntity, PersonId extends Long> implements PersonService <Person, PersonId>{

    @Autowired
    PersonRepository<Person, PersonId> personRepository;

    @Override
    public Long login(String username, String password) {


        Person personEntity;

        try{
            personEntity = personRepository.findByUsername(username);
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        if (!personEntity.getPassword().equals(password)) {
            throw new InvalidCredentials();
        }

        return personEntity.getId();

    }

    @Override
    public void updateBalance(Double valueIncrement, PersonId id) {

        Person user;

        try{
            user =  personRepository.findById(id).get();
        }
        catch(NoSuchElementException ex){
            throw new UserNotFound();
        }

        user.updateBalance(valueIncrement);
        personRepository.save(user);


    }


}
