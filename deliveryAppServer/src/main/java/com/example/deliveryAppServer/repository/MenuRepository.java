package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.order.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,Long> {
}
