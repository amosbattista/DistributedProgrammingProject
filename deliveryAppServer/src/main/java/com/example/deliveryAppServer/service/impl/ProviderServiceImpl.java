package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.DishNotFound;
import com.example.deliveryAppServer.exception.UserAlreadyExists;
import com.example.deliveryAppServer.exception.UserNotFound;
import com.example.deliveryAppServer.model.dao.order.DishEntity;
import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.repository.DishRepository;
import com.example.deliveryAppServer.repository.MenuRepository;
import com.example.deliveryAppServer.repository.ProviderRepository;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProviderServiceImpl extends PersonServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    ProviderServiceImpl(ProviderRepository providerRepository){
        personRepository = providerRepository;
    }

    @Override
    public ProviderEntity createNewProvider(ProviderEntity provider) {
        provider.setBalance(0);
        if(providerRepository.existsByUsername(provider.getUsername())){
            throw new UserAlreadyExists("Customer "+ provider.getUsername()+" already exists!");
        }

        if(providerRepository.existsByTelephoneNumber(provider.getTelephoneNumber())){
            throw new UserAlreadyExists("Customer tel. number "+ provider.getTelephoneNumber()+" already exists!");
        }


        provider = providerRepository.save(provider);
        log.info("[SERVICE] New customer "+provider.getUsername()+" created!");
        return provider;
    }

    @Override
    public ProviderEntity updateProvider(ProviderEntity newProvider) {
        if(newProvider.getId()==null ||!providerRepository.existsById(newProvider.getId())){
            throw new UserNotFound();
        }

        ProviderEntity prevProvider = providerRepository.getById(newProvider.getId());

        if(providerRepository.existsByUsernameExceptMyself(newProvider.getId(),newProvider.getUsername())){
            throw new UserAlreadyExists("Username "+newProvider.getUsername()+" not available");
        }

        if(providerRepository.existsByTelephoneNumberExceptMyself(newProvider.getId(), newProvider.getTelephoneNumber())){
            throw new UserAlreadyExists("Telephone Number "+newProvider.getTelephoneNumber()+" not available");
        }

        newProvider.setBalance(prevProvider.getBalance());

        if(newProvider.getPassword()==null || newProvider.getPassword().isBlank())
            newProvider.setPassword(prevProvider.getPassword());

        if(newProvider.getMenu()==null)
            newProvider.setMenu(prevProvider.getMenu());



        newProvider = providerRepository.save(newProvider);
        log.info("[SERVICE]"+newProvider.getUsername()+" successfully updated!");
        return newProvider;

    }


    @Override
    public List<ProviderEntity> getAvailableProviders() {
        return providerRepository.findAllByIsAvailable(true);
    }

    @Override
    public void setAvailability(Boolean avail, Long id) {
        ProviderEntity prevProvider = providerRepository.getById(id);
        prevProvider.setIsAvailable(avail);
        log.info("[SERVICE]"+"availability set to: "+avail);
        providerRepository.save(prevProvider);
    }

    @Override
    public void createNewMenu(MenuEntity newMenu) {


        MenuEntity savedMenu = menuRepository.save(newMenu);

        for (DishEntity dish: savedMenu.getDishEntities())
            dish.setMenu(savedMenu);

        menuRepository.save(savedMenu);

        ProviderEntity provider = providerRepository.getById(newMenu.getProvider().getId());
        provider.setMenu(savedMenu);
        providerRepository.save(provider);
        log.info("[SERVICE] created new menu for provider: "+savedMenu.getProvider().getId());
    }

    @Override
    public MenuEntity getMenu(Long providerId) {
        ProviderEntity prov = providerRepository.getById(providerId);
        MenuEntity menu = prov.getMenu();

        if(menu == null){
            menu = new MenuEntity();
            menu.setProvider(prov);
            prov.setMenu(menuRepository.save(menu));
            providerRepository.save(prov);
        }

        log.info("[SERVICE] get menu for provider: "+providerId);
        return menu;
    }

    @Override
    public DishEntity addDish(DishEntity dish, Long providerId) {
        ProviderEntity prov = providerRepository.getById(providerId);
        MenuEntity menu = prov.getMenu();

        dish.setMenu(menu);

        return dishRepository.save(dish);

    }

    @Override
    public DishEntity updateDish(DishEntity dish, Long providerId) {
        ProviderEntity prov = providerRepository.getById(providerId);
        MenuEntity menu = prov.getMenu();

        if(menu==null || !menu.getDishEntities().contains(dish)){

            throw new DishNotFound();
        }

        dish.setMenu(menu);

        return dishRepository.save(dish);
    }

    @Override
    public void removeDish(Long dishId, Long providerId) {
        DishEntity dish = dishRepository.getById(dishId);
        dish.setMenu(null);
        dishRepository.save(dish);
    }


}

