package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.model.dao.user.PersonEntity;

public interface PersonService<Person extends PersonEntity, PersonId extends Long> {

    public Long login(String username, String password);
    public void updateBalance(Double valueIncrement, PersonId id);
    public PersonEntity getPerson(PersonId personId);
}
