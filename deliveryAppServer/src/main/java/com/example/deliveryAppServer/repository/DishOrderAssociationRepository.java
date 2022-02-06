package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.order.DishOrderAssociation;
import com.example.deliveryAppServer.model.dao.order.DishOrderAssociationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * It allows the DishOrderAssociation to persist in the database.
 * It defines and builds all the standard CRUD DB queries for the DishOrderAssociation.
 */
@Repository
public interface DishOrderAssociationRepository extends JpaRepository<DishOrderAssociation, DishOrderAssociationKey> {

}
