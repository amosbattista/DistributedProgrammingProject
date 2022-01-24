package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.ProviderEntity;

import java.util.List;

public interface ProviderService {
    public void createNewProvider(ProviderEntity provider);
    public void updateProvider(ProviderEntity provider);
    public List<ProviderEntity> getAvailableProviders();
    public Long loginProvider(String username, String password);
}
