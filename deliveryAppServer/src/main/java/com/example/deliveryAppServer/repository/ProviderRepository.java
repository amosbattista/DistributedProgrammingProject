package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity,Long> {
}
