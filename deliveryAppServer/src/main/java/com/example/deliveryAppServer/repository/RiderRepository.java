package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/**
 * It allows the RiderEntity to persist in the database. It defines and builds all the standard CRUD DB queries for the RiderEntity.
 * It allows adding new custom queries.
 */
@Repository
public interface RiderRepository extends JpaRepository<RiderEntity,Long>, PersonRepository<RiderEntity,Long> {

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
     * @return the ProviderEntity with the given username
     */
    RiderEntity findByUsername(String username);

    /**
     *
     * @param id
     * @param username
     * @return True if exists an entity with the given username except for the rider with the given id,
     *         otherwise False
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.id != :id AND u.username = :username")
    boolean existsByUsernameExceptMyself(@Param("id") long id, @Param("username") String username);

    /**
     *
     * @param id
     * @param telephoneNumber
     * @return True if exists an entity with the given telephoneNumber except for the rider with the given id,
     *         otherwise False
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.id != :id AND u.telephoneNumber = :telephoneNumber")
    boolean existsByTelephoneNumberExceptMyself(@Param("id") long id, @Param("telephoneNumber") String telephoneNumber);
}
