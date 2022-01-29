package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.dao.order.DishOrderAssociationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishOrderAssociationRepository extends JpaRepository<DishOrderAssociation, DishOrderAssociationKey> {
}
