package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.ProviderEntity;

import java.util.List;

public interface ProviderService {
    public void createNewProvider(ProviderEntity provider);
    public void updateProvider(ProviderEntity provider);
    public void decreaseBalanceProvider(Long id, Double value);
    public void encreaseBalanceProvider(Long id, Double value);
    public List<ProviderEntity> getAvailableProviders();
}
