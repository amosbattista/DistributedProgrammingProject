package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity,Long>, PersonRepository<ProviderEntity,Long> {

    boolean existsByUsername(String username);
    boolean existsByTelephoneNumber(String telephoneNumber);
    List<ProviderEntity> findAllByIsAvailable(boolean IsAvailable);

    ProviderEntity findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END" +
            " FROM ProviderEntity ue " +
            "WHERE ue.id != :providerId and ue.username = :providerUsername")
    boolean existsByUsernameExceptMyself(@Param("providerId")Long providerId, @Param("providerUsername")String username);

    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END " +
            " FROM ProviderEntity ue " +
            "WHERE ue.id != :providerId and ue.telephoneNumber = :providerTelephone")
    boolean existsByTelephoneNumberExceptMyself(@Param("providerId")Long providerId, @Param("providerTelephone")String telephoneNumber);
}
