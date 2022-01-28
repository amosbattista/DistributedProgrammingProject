package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.order.DishEntity;
import com.example.deliveryAppServer.model.order.MenuEntity;
import com.example.deliveryAppServer.model.user.ProviderEntity;

import java.util.List;

public interface ProviderService extends PersonService {
    public void createNewProvider(ProviderEntity provider);
    public void updateProvider(ProviderEntity provider);
    public List<ProviderEntity> getAvailableProviders();

    void setAvailability(Boolean avail, Long providerId);

    void createNewMenu(MenuEntity menu);


    MenuEntity getMenu(Long providerId);

    DishEntity addDish(DishEntity dish,Long providerId);
}
