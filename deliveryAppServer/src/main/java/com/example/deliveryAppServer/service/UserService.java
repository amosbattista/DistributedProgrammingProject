package com.example.deliveryAppServer.service;

import com.example.deliveryAppServer.model.user.PersonEntity;
import com.example.deliveryAppServer.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService {

    public Long login(String username, String password, UserRepository repository);
    public void updateBalance(Double value, Long id, UserRepository<PersonEntity, Long> repository);
}
