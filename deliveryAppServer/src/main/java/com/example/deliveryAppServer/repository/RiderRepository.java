package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.RiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends JpaRepository<RiderEntity,Long>, UserRepository<RiderEntity,Long> {

    boolean existsByUsername(String username);
    boolean existsByTelephoneNumber(String telephoneNumber);
    RiderEntity findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.id != :id AND u.username = :username")
    boolean existsByUsernameExceptMyself(@Param("id") long id, @Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.id != :id AND u.telephoneNumber = :telephoneNumber")
    boolean existsByTelephoneNumberExceptMyself(@Param("id") long id, @Param("telephoneNumber") String telephoneNumber);
}
