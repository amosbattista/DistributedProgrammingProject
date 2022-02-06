package com.example.deliveryAppServer.service.impl;


import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.repository.CustomerRepository;
import com.example.deliveryAppServer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * It defines and holds the app business logic, regarding the operations that act on the Customer entity.
 * It performs CRUD queries on the database by using an instance of the Customer's Repository.
 */
@Service
@Slf4j
public class CustomerServiceImpl extends PersonServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository){
        personRepository = customerRepository;
    }

    /**
     * It creates a new customer into the database.
     *
     * @param customer is the Customer to be created
     * @return the CustomerEntity just created
     * @throws UserAlreadyExists if the customer to be created already exists, checking a violation on the
     * on unique fields (username and telephoneNumber).
     */
    @Override
    public CustomerEntity createNewCustomer(CustomerEntity customer) {

        if(customerRepository.existsByUsername(customer.getUsername())){
            throw new UserAlreadyExists("Customer "+ customer.getUsername()+" already exists!");
        }

        if(customerRepository.existsByTelephoneNumber(customer.getTelephoneNumber())){
            throw new UserAlreadyExists("Customer tel. number "+ customer.getTelephoneNumber()+" already exists!");
        }
        customer.setBalance(0);
        CustomerEntity cust = customerRepository.save(customer);
        log.info("[SERVICE] New customer "+customer.getUsername()+" created!");
        return cust;
    }

    /**
     * It updates an existing customer on database, if he exists. It prevents
     * the balance from being updated via this method.
     *
     * @param newCustomer is the Customer to be updated
     * @return the CustomerEntity just updated
     * @throws UserNotFound if the given customer is not present on the database
     * @throws UserAlreadyExists if the given customer violates unique fields (username and telephoneNumber)
     */
    @Override
    public CustomerEntity updateCustomer(CustomerEntity newCustomer) {

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


        CustomerEntity customer2 = customerRepository.save(newCustomer);
        log.info("[SERVICE]"+newCustomer.getUsername()+" successfully updated!");
        return customer2;


    }


}
