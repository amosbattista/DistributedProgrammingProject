package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.PersonEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository<Person extends PersonEntity, PersonId extends Long> extends JpaRepository<Person, PersonId> {

    public Person findByUsername(String username);


}
