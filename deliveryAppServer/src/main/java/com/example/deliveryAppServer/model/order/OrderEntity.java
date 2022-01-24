package com.example.deliveryAppServer.model.order;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.example.deliveryAppServer.model.user.RiderEntity;
import com.example.deliveryAppServer.model.user.UserEntity;
import lombok.Data;
import com.example.deliveryAppServer.model.user.CustomerEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToMany
    @JoinTable(
            name = "dish_ordered",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private List<DishEntity> dishList;

//
    @ManyToOne(targetEntity= CustomerEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="customerId")
    private CustomerEntity customer;

    @ManyToOne( targetEntity= ProviderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="providerId")
    private ProviderEntity provider;

    @ManyToOne(targetEntity= RiderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="riderId")
    private RiderEntity rider;


    @NotBlank
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @NotBlank
    private LocalDateTime deliveryTime;

    private static final int minuteOffsetDeliveryTime = 10;


}
