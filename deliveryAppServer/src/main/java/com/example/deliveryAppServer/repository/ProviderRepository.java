package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * It allows the ProviderEntity to persist in the database. It defines and builds all the standard CRUD DB queries for the ProviderEntity.
 * It allows adding new custom queries.
 */
@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity,Long>, PersonRepository<ProviderEntity,Long> {

    /**
     *
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
     * @param isAvailableParam
     * @return a list of all ProviderEntity where the field isAvailable = isAvailableParam
     */
    List<ProviderEntity> findAllByIsAvailable(boolean isAvailableParam);

    /**
     *
     * @param username
     * @return the ProviderEntity with the given username
     */
    ProviderEntity findByUsername(String username);

    /**
     *
     * @param providerId
     * @param username
     * @return True if exists an entity with the given username except for the provider with the given providerId,
     *         otherwise False
     */
    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END" +
            " FROM ProviderEntity ue " +
            "WHERE ue.id != :providerId and ue.username = :providerUsername")
    boolean existsByUsernameExceptMyself(@Param("providerId")Long providerId, @Param("providerUsername")String username);

    /**
     *
     * @param providerId
     * @param telephoneNumber
     * @return True if exists an entity with the given telephoneNumber except for the provider with the given providerId,
     *         otherwise False
     */
    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END " +
            " FROM ProviderEntity ue " +
            "WHERE ue.id != :providerId and ue.telephoneNumber = :providerTelephone")
    boolean existsByTelephoneNumberExceptMyself(@Param("providerId")Long providerId, @Param("providerTelephone")String telephoneNumber);
}
