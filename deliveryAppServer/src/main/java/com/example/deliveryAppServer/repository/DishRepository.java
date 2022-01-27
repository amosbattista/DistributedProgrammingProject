package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.order.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishEntity,Long> {
}
