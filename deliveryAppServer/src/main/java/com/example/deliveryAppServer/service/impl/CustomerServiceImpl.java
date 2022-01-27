package com.example.deliveryAppServer.service.impl;


import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import com.example.deliveryAppServer.repository.CustomerRepository;
import com.example.deliveryAppServer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class CustomerServiceImpl extends PersonServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository){
        personRepository = customerRepository;
    }

    @Override
    public void createNewCustomer(CustomerEntity customer) {

        if(customerRepository.existsByUsername(customer.getUsername())){
            throw new UserAlreadyExists("Customer "+ customer.getUsername()+" already exists!");
        }

        if(customerRepository.existsByTelephoneNumber(customer.getTelephoneNumber())){
            throw new UserAlreadyExists("Customer tel. number "+ customer.getTelephoneNumber()+" already exists!");
        }


        customerRepository.save(customer);
        log.info("[SERVICE] New customer "+customer.getUsername()+" created!");



    }

    @Override
    public void updateCustomer(CustomerEntity customer) {

        if(!customerRepository.existsById(customer.getId())){
            throw new UserNotFound();
        }

        if(customerRepository.existsByUsernameExceptMyself(customer.getId(), customer.getUsername())){
            throw new UserAlreadyExists("Username "+customer.getUsername()+" not available");
        }

        if(customerRepository.existsByTelephoneNumberExceptMyself(customer.getId(),customer.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+customer.getTelephoneNumber()+" not available");
        }



        log.info("[SERVICE]"+customer.getUsername()+" successfully updated!");
        customerRepository.save(customer);
     //   updateBalance(20.12, customer.getId(), customerRepository);
      //

    }


}
