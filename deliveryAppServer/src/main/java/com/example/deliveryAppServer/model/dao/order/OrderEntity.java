package com.example.deliveryAppServer.model.dao.order;

import com.example.deliveryAppServer.model.dao.user.CustomerEntity;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;
import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.example.deliveryAppServer.model.dao.user.RiderEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  It's a concrete entity of the system that will be stored persistently in DB.
 *  It represents an order, the 'id' field represents its primary key in the DB.
 *  Each order maintains the references to the customer, the riders and provider, with a many-to-one DB constraint.
 *
 *  Each order can be of four types (identified by the field orderType), according to the delivery type:
 *
 *      - TAKE_AWAY: if it is take-away delivery
 *
 *      - DELIVERY: if it is a home-delivery and the type of rider has not yet been specified
 *
 *      - DELIVERY_RIDERS: if it is a home-delivery, made with the app rider
 *
 *      - DELIVERY_NORIDER: if it is a home-delivery, made with the provider's rider
 *
 * Each order it can be in one of these six states (identified by the field orderState):
 *
 *      - PENDING: the order was created by the customer but has not yet been accepted by the provider
 *
 *      - ACCEPTED: the order has been fully accepted by the provider and is waiting to be prepared and delivered
 *
 *      - SEMI-ACCEPTED: the order has been accepted by the provider, but not yet by a rider (expected only if the
 *                       order is of type DELIVERY_RIDERS)
 *
 *      - SHIPPED: the delivery of the order has started (not expected if the order is take-away)
 *
 *      - COMPLETED: the order has been delivered to the customer
 *
 *      - REFUSED: the order was refused by provider or no riders were found within the time indicated by the field
 *                 minuteOffsetDeliveryTime.
 *
 *
 * Each order maintains a list of own dishOrderAssociation, for this reason a one-to-many DB constraint
 * is specified on 'orderdishOrderAssociationsList' field (each order can have multiple associations,
 * but one association belongs to only one order). It's used to maintain the dishes and their quantity.
 *
 * For each order the date and time of delivery is specified through the field 'deliveryTime'.
 */
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

    public static final int minuteOffsetDeliveryTime = 10;




}
