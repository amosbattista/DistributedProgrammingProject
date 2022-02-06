package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * It allows the MenuEntity to persist in the database.
 * It defines and builds all the standard CRUD DB queries for the MenuEntity.
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,Long> {
}
