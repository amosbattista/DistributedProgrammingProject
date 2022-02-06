package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.user.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * It allows the PersonEntity to persist in the database. It defines and builds all the standard CRUD DB queries for the PersonEntity.
 * It allows adding new custom queries.
 */
public interface PersonRepository<Person extends PersonEntity, PersonId extends Long> extends JpaRepository<Person, PersonId> {

    public Person findByUsername(String username);


}
