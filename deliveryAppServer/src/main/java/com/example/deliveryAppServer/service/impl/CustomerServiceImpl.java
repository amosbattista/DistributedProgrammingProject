package com.example.deliveryAppServer.service.impl;


import com.example.deliveryAppServer.exception.OrderAlreadyInProgress;
import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import com.example.deliveryAppServer.repository.CustomerRepository;
import com.example.deliveryAppServer.repository.OrderRepository;
import com.example.deliveryAppServer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.deliveryAppServer.model.enumerations.OrderState.COMPLETED;
import static com.example.deliveryAppServer.model.enumerations.OrderState.REFUSED;

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
    public void updateCustomer(CustomerEntity newCustomer) {

        if(newCustomer.getId()==null || !customerRepository.existsById(newCustomer.getId())){
            throw new UserNotFound();
        }

        CustomerEntity prevCustomer = customerRepository.getById(newCustomer.getId());

        if(customerRepository.existsByUsernameExceptMyself(newCustomer.getId(), newCustomer.getUsername())){
            throw new UserAlreadyExists("Username "+newCustomer.getUsername()+" not available");
        }

        if(customerRepository.existsByTelephoneNumberExceptMyself(newCustomer.getId(),newCustomer.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+newCustomer.getTelephoneNumber()+" not available");
        }

        newCustomer.setBalance(prevCustomer.getBalance());

        if(newCustomer.getPassword()==null || newCustomer.getPassword().isBlank())
            newCustomer.setPassword(prevCustomer.getPassword());


        customerRepository.save(newCustomer);
        log.info("[SERVICE]"+newCustomer.getUsername()+" successfully updated!");



     //   updateBalance(20.12, customer.getId(), customerRepository);
      //

    }



}
