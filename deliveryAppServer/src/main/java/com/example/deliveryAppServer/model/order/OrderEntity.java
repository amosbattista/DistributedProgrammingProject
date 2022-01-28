package com.example.deliveryAppServer.model.order;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.example.deliveryAppServer.model.user.RiderEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) //Non necessario
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToMany(targetEntity=DishOrderAssociation.class,cascade = CascadeType.MERGE , fetch = FetchType.LAZY, mappedBy = "order")
    @NotNull
    private List<DishOrderAssociation> dishOrderAssociations;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @ManyToOne(targetEntity= CustomerEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="customerId", nullable = false)
    private CustomerEntity customer;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @ManyToOne( targetEntity= ProviderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="providerId", nullable = false)
    private ProviderEntity provider;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity= RiderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="riderId")
    private RiderEntity rider;


    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @NotNull
    private LocalDateTime deliveryTime;

    private double price;

    private static final int minuteOffsetDeliveryTime = 10;




}
