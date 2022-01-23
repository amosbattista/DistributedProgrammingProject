package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {

    boolean existsByUsername(String username);
    boolean existsByIban(String iban);
    boolean existsByTelephoneNumber(String telephoneNumber);
}
