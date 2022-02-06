package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.order.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * It allows the DishEntity to persist in the database.
 * It defines and builds all the standard CRUD DB queries for the DishEntity.
 */
@Repository
public interface DishRepository extends JpaRepository<DishEntity,Long> {
}
