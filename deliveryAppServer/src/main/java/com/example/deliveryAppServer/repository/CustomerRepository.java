package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long>, PersonRepository<CustomerEntity, Long> {

    boolean existsByUsername(String username);
    boolean existsByIban(String iban);
    boolean existsByTelephoneNumber(String telephoneNumber);

    CustomerEntity findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END" +
            " FROM CustomerEntity ue " +
            "WHERE ue.id != :customerId and ue.username = :customerUsername")
    boolean existsByUsernameExceptMyself(@Param("customerId")Long customerId, @Param("customerUsername")String username);

    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END " +
            " FROM CustomerEntity ue " +
            "WHERE ue.id != :customerId and ue.telephoneNumber = :customerTelephone")
    boolean existsByTelephoneNumberExceptMyself(@Param("customerId")Long customerId, @Param("customerTelephone")String telephoneNumber);
}
