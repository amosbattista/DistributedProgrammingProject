package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.ProviderEntity;

import java.util.List;

public interface ProviderService extends PersonService {
    public void createNewProvider(ProviderEntity provider);
    public void updateProvider(ProviderEntity provider);
    public List<ProviderEntity> getAvailableProviders();
}
