package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.example.deliveryAppServer.repository.CustomerRepository;
import com.example.deliveryAppServer.repository.ProviderRepository;
import com.example.deliveryAppServer.service.CustomerService;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public void createNewProvider(ProviderEntity provider) {
        provider.setBalance(0);
        if(providerRepository.existsByUsername(provider.getUsername())){
            throw new UserAlreadyExists("Customer "+ provider.getUsername()+" already exists!");
        }

        if(providerRepository.existsByTelephoneNumber(provider.getTelephoneNumber())){
            throw new UserAlreadyExists("Customer tel. number "+ provider.getTelephoneNumber()+" already exists!");
        }


        providerRepository.save(provider);
        log.info("[SERVICE] New customer "+provider.getUsername()+" created!");
    }

    @Override
    public void updateProvider(ProviderEntity provider) {
        if(!providerRepository.existsById(provider.getId())){
            throw new UserNotFound();
        }

        if(providerRepository.existsByUsernameExceptMyself(provider.getId(),provider.getUsername())){
            throw new UserAlreadyExists("Username "+provider.getUsername()+" not available");
        }

        if(providerRepository.existsByTelephoneNumberExceptMyself(provider.getId(), provider.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+provider.getTelephoneNumber()+" not available");
        }


        providerRepository.save(provider);
        log.info("[SERVICE]"+provider.getUsername()+" successfully updated!");

    }

    @Override
    public void decreaseBalanceProvider(Long id, Double value) {
        ProviderEntity provider;
        try{
            provider = providerRepository.findById(id).get();
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }
        Double oldBalance = provider.getBalance();
        Double newBalance = oldBalance - value;
        provider.setBalance(newBalance);
        providerRepository.save(provider);

    }

    @Override
    public void encreaseBalanceProvider(Long id, Double value) {
        ProviderEntity provider;
        try{
            provider = providerRepository.findById(id).get();
        }catch(NoSuchElementException ex){
            throw new UserNotFound();
        }
        Double oldBalance = provider.getBalance();
        Double newBalance = oldBalance + value;
        provider.setBalance(newBalance);
        providerRepository.save(provider);
    }

    @Override
    public List<ProviderEntity> getAvailableProviders() {
        return providerRepository.findAllByIsAvailable(true);
    }
}

