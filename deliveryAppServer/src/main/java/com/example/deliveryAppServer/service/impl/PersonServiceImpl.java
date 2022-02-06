package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.InvalidCredentials;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.model.dao.user.PersonEntity;
import com.example.deliveryAppServer.repository.PersonRepository;
import com.example.deliveryAppServer.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * It defines and holds the app business logic, regarding the operations that act on the Person entity.
 * It performs CRUD queries on the database by using an instance of the Person Repository.
 */
@Service
public class PersonServiceImpl <Person extends PersonEntity, PersonId extends Long> implements PersonService <Person, PersonId>{

    @Autowired
    PersonRepository<Person, PersonId> personRepository;


    /**
     * It retrieves the user ID from the database, checking if the credentials are correct
     * @param username is the username of the Person who want to be logged
     * @param password is the password of the Person who want to be logged
     * @throws InvalidCredentials if the given username is not on the database or if the
     * given password does not match with the correct one.
     * @return the user ID
     */
    @Override
    public Long login(String username, String password) {

        Person personEntity;


        personEntity = personRepository.findByUsername(username);

        if (personEntity==null || !personEntity.getPassword().equals(password) ) {
            throw new InvalidCredentials();
        }

        return personEntity.getId();

    }

    /**
     * It updates an existing person on database, if he exists, changing only his balance
     * @param valueIncrement is the increment to be added to the previous balance value (it can be negative)
     * @param id identify the Person to be updated
     */
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

    @Override
    public PersonEntity getPerson(PersonId personId) {

            if(!personRepository.existsById(personId)){
                throw new UserNotFound();
            }

            return personRepository.findById(personId).get();

    }


}
