package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * It allows the CustomerEntity to persist in the database. It defines and builds all the standard CRUD DB queries for the CustomerEntity.
 * It allows adding new custom queries.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long>, PersonRepository<CustomerEntity, Long> {

    /**
     * @param username
     * @return True if an entity with the given username exists, otherwise False
     */
    boolean existsByUsername(String username);

    /**
     *
     * @param telephoneNumber
     * @return True if an entity with the given telephoneNumber exists, otherwise False
     */
    boolean existsByTelephoneNumber(String telephoneNumber);

    /**
     *
     * @param username
     * @return the CustomerEntity with the given username
     */
    CustomerEntity findByUsername(String username);

    /**
     *
     * @param customerId
     * @param username
     * @return True if exists an entity with the given username except for the customer with the given customerId,
     *         otherwise False
     */
    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END" +
            " FROM CustomerEntity ue " +
            "WHERE ue.id != :customerId and ue.username = :customerUsername")
    boolean existsByUsernameExceptMyself(@Param("customerId")Long customerId, @Param("customerUsername")String username);

    /**
     *
     * @param customerId
     * @param telephoneNumber
     * @return True if exists an entity with the given telephoneNumber except for the customer with the given customerId,
     *         otherwise False
     */
    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END " +
            " FROM CustomerEntity ue " +
            "WHERE ue.id != :customerId and ue.telephoneNumber = :customerTelephone")
    boolean existsByTelephoneNumberExceptMyself(@Param("customerId")Long customerId, @Param("customerTelephone")String telephoneNumber);
}
