package com.example.deliveryAppServer.model.dao.user;

import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.example.deliveryAppServer.model.dao.order.MenuEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * It's a concrete entity of the system that will be stored persistently in DB.
 * It represents a provider, which aims to offer its own menu to customers.
 *
 * Each provider can specify the order delivery method that he offers:
 * - doTakeAway: is true if the provider do a take-away delivery
 * - doDelivering: is true if the provider do home delivery
 *
 * Moreover, the provider can specify whether he has its own riders with the field 'hasOwnRiders'.
 *
 * The provider can specify whether he is available with the field 'isAvailable'
 *
 * Each provider maintains a list of own orders, for this reason a one-to-many DB constraint
 * is specified on 'orderList' field (each provider can have multiple orders, but one order belongs to only one provider)
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProviderEntity extends PersonEntity implements Serializable {

    @NotBlank
    private String providerName;

    @NotBlank
    private String cuisine; //type of cuisine

    @NotBlank
    private String address;

    @NotNull
    private Boolean doDelivering;

    @NotNull
    private Boolean doTakeAway;

    @NotNull
    private Boolean hasOwnRiders;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //one-to-one DB constraint
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private MenuEntity menu;

    @NotNull
    private Boolean isAvailable;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //one-to-many DB constraints
    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "provider")
    private List<OrderEntity> orderList;


}
