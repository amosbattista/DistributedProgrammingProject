package com.example.deliveryAppServer.service.impl;

import com.example.deliveryAppServer.exception.*;
import com.example.deliveryAppServer.model.dao.order.DishEntity;
import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.repository.DishRepository;
import com.example.deliveryAppServer.repository.MenuRepository;
import com.example.deliveryAppServer.repository.ProviderRepository;
import com.example.deliveryAppServer.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * It defines and holds the app business logic, regarding the operations that act on the Provider entity.
 * It performs CRUD queries on the database by using instance of the Provider's Repository, Menu Repository and
 * Dish Repository.
 */
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

    /**
     * It creates a new provider into the database.
     *
     * @param provider is the Provider to be created
     * @return the ProviderEntity just created
     * @throws UserAlreadyExists if the provider to be created already exists, checking a violation on the
     * on unique fields (username and telephoneNumber).
     */
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

    /**
     * It updates an existing provider on database, if he exists. It prevents
     * the balance from being updated via this method.
     *
     * @param newProvider is the Provider to be updated
     * @return the ProviderEntity just updated
     * @throws UserNotFound if the given provider is not present on the database
     * @throws UserAlreadyExists if the given provider violates unique fields (username and telephoneNumber)
     */
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


    /**
     * It retrieves from the database all the ProviderEntity with the field isAvailable=true.
     * @return a List<ProviderEntity>
     */
    @Override
    public List<ProviderEntity> getAvailableProviders() {
        return providerRepository.findAllByIsAvailable(true);
    }

    /**
     * It set the field isAvailable of the provider, identified by his ID
     * @param avail is the new value of isAvailable
     * @param id is the provider ID
     */
    @Override
    public void setAvailability(Boolean avail, Long id) {
        ProviderEntity prevProvider = providerRepository.getById(id);
        prevProvider.setIsAvailable(avail);
        log.info("[SERVICE]"+"availability set to: "+avail);
        providerRepository.save(prevProvider);
    }


    /**
     * It creates a new menu into the database and associates it with the proper provider
     * @param newMenu is the Menu to be created
     */
    @Override
    public void createNewMenu(MenuEntity newMenu) {

        MenuEntity savedMenu = menuRepository.save(newMenu);

        for (DishEntity dish: savedMenu.getDishList())
            dish.setMenu(savedMenu);

        menuRepository.save(savedMenu);

        ProviderEntity provider = providerRepository.getById(newMenu.getProvider().getId());
        provider.setMenu(savedMenu);
        providerRepository.save(provider);
        log.info("[SERVICE] created new menu for provider: "+savedMenu.getProvider().getId());
    }

    /**
     * It retrieves from database the provider's menu
     * @param providerId is the provider ID
     * @return the MenuEntity
     */
    @Override
    public MenuEntity getMenu(Long providerId) {
        ProviderEntity prov = providerRepository.getById(providerId);
        MenuEntity menu = prov.getMenu();

        if(menu == null){
            menu = new MenuEntity();
            menu.setProvider(prov);
            menu.setDishList(new LinkedList<DishEntity>());
            prov.setMenu(menuRepository.save(menu));
            providerRepository.save(prov);
        }

        log.info("[SERVICE] get menu for provider: "+providerId);
        return menu;
    }

    /**
     * It creates a new dish in the database and associates it with the proper provider menu
     *
     * @param dish is the dish to be added
     * @param providerId is the provider ID
     * @return the DishEntity just added
     * @throws DishAlreadyPresent if the dish is already present in the menu
     */
    @Override
    public DishEntity addDish(DishEntity dish, Long providerId) {
        ProviderEntity prov = providerRepository.getById(providerId);
        MenuEntity menu = prov.getMenu();

        dish.setMenu(menu);

        try {
            dish = dishRepository.save(dish);
        }
        catch (DataIntegrityViolationException ex){
            throw new DishAlreadyPresent();

        }
        log.info("[SERVICE] add new dish for provider: "+providerId);
        return dish;
    }


    /**
     * It updates an existing dish on database, if it exists and if it is associated to the provider menu
     * @param dish is the Dish to be updated
     * @param providerId is the provider ID
     * @return the DishEntity just updated
     * @throws DishNotFound if the dish is not present on the provider menu
     * @throws DishAlreadyPresent if the given dish violets unique fields
     */
    @Override
    public DishEntity updateDish(DishEntity dish, Long providerId) {
        ProviderEntity prov = providerRepository.getById(providerId);
        MenuEntity menu = prov.getMenu();

        if(menu==null || !menu.getDishList().contains(dish)){

            throw new DishNotFound();
        }

        dish.setMenu(menu);

        try {
            dish = dishRepository.save(dish);
        }
        catch (DataIntegrityViolationException ex){
            throw new DishAlreadyPresent();

        }

        log.info("[SERVICE] update a dish for provider: "+providerId);
        return dish;
    }


    /**
     * It deletes a given dish from a given provider.
     * @param dishId is the dish ID to be deleted
     * @param providerId is the provider ID
     */
    @Override
    public void removeDish(Long dishId, Long providerId) {
        DishEntity dish = dishRepository.getById(dishId);
        dish.setMenu(null);
        dishRepository.save(dish);
        log.info("[SERVICE] remove a dish for provider: "+providerId);
    }


}

