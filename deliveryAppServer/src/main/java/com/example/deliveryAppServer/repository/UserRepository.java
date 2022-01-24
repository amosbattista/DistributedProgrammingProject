package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository<T extends UserEntity,ID extends Long> extends JpaRepository<T, ID> {

    public UserEntity findByUsername(String username);


}
