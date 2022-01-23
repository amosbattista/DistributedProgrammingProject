package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends JpaRepository<RiderEntity,Long> {
}
