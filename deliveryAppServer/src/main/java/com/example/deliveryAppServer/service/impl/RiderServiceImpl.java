package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import com.example.deliveryAppServer.repository.RiderRepository;
import com.example.deliveryAppServer.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * It defines and holds the app business logic, regarding the operations that act on the Rider entity.
 * It performs CRUD queries on the database by using an instance of the Rider's Repository.
 */
@Service
@Slf4j
public class RiderServiceImpl extends PersonServiceImpl implements RiderService {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    public RiderServiceImpl(RiderRepository riderRepository){
        personRepository = riderRepository;
    }

    /**
     * It creates a new rider into the database.
     * @param rider is the Rider to be created
     * @return the RiderEntity just created
     * @throws UserAlreadyExists if the rider to be created already exists, checking a violation on the
     * on unique fields (username and telephoneNumber).
     */
    @Override
    public RiderEntity createNewRider(RiderEntity rider){

        if(riderRepository.existsByUsername(rider.getUsername())){
            throw new UserAlreadyExists("Rider " + rider.getUsername() + " already exists!");   //NON FUNZIONA CON ALTRE ENTITA'
        }

        if(riderRepository.existsByTelephoneNumber(rider.getTelephoneNumber())){
            throw new UserAlreadyExists("Rider tel. number " + rider.getTelephoneNumber() + " already exists!"); //NON FUNZIONA CON ALTRE ENTITA'
        }

        rider = riderRepository.save(rider);
        log.info("[SERVICE] New rider " + rider.getUsername() + " created!");
        return rider;
    }

    /**
     * It updates an existing rider on database, if he exists. It prevents
     * the balance from being updated via this method.
     *
     * @param newRider is the Customer to be updated
     * @return the RiderEntity just updated
     * @throws UserNotFound if the given rider is not present on the database
     * @throws UserAlreadyExists if the given rider violates unique fields (username and telephoneNumber)
     */
    @Override
    public RiderEntity updateRider(RiderEntity newRider){

        if(newRider.getId()==null || !riderRepository.existsById(newRider.getId())){
            throw new UserNotFound();
        }

        RiderEntity prevRider = riderRepository.getById(newRider.getId());

        if(riderRepository.existsByUsernameExceptMyself(newRider.getId(), newRider.getUsername())){
            throw new UserAlreadyExists("Username " +newRider.getUsername()+" not available");
        }

        if(riderRepository.existsByTelephoneNumberExceptMyself(newRider.getId(), newRider.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+newRider.getTelephoneNumber()+" not available");
        }


        newRider.setBalance(prevRider.getBalance());

        if(newRider.getPassword()==null || newRider.getPassword().isBlank())
            newRider.setPassword(prevRider.getPassword());


        newRider = riderRepository.save(newRider);
        log.info("[SERVICE]"+newRider.getUsername()+" successfully updated!");
        return newRider;
    }


}
