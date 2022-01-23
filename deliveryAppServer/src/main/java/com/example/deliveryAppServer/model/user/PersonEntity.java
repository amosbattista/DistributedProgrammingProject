package com.example.deliveryAppServer.model.user;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
public abstract class PersonEntity extends UserEntity implements Serializable {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String surname;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthDate;

    private String iban;

    @Column(unique = true)
    @Pattern(regexp ="^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$", message = "Wrong telephone number!")
    private String telephoneNumber;

    private double balance;




}
