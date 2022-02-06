package com.example.deliveryAppServer.model.dao.user;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * It's an abstract system entity.
 * Represents the parent class of all system users.
 * It consists of a unique self-generated id, a unique username and password.
 * This class will then be mapped into the database considering 'id' as primary key.
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //create one table on DB for each concrete class that inherits it
public abstract class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
