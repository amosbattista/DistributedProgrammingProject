package com.example.deliveryAppServer.model.dao.user;

import com.example.deliveryAppServer.exception.InsufficientBalanceException;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    @NotBlank
    private String iban;

    @NotBlank
    @Column(unique = true)
    @Pattern(regexp ="^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$", message = "Wrong telephone number!")
    private String telephoneNumber;


    private double balance;

    public void updateBalance(double increment) {
        if(getBalance() + increment < 0){
            throw new InsufficientBalanceException(); //da cambiare
        }

        balance = balance+increment;
    }



}
