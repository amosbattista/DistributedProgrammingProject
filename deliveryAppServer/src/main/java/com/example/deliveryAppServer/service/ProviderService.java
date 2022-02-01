package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.dao.order.DishEntity;
import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dto.user.ProviderDto;

import java.util.List;

public interface ProviderService extends PersonService {
    public ProviderEntity createNewProvider(ProviderEntity provider);
    public ProviderEntity updateProvider(ProviderEntity provider);
    public List<ProviderEntity> getAvailableProviders();

    void setAvailability(Boolean avail, Long providerId);

    void createNewMenu(MenuEntity menu);


    MenuEntity getMenu(Long providerId);

    DishEntity addDish(DishEntity dish,Long providerId);
    DishEntity updateDish(DishEntity dish,Long providerId);

    void removeDish(Long dishId, Long providerId);
}
