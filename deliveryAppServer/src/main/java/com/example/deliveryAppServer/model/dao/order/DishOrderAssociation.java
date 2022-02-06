package com.example.deliveryAppServer.model.dao.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  It's a concrete entity of the system that will be stored persistently in DB.
 *  It consists of a DishEntity and a OrderEntity that together represent a multiple primary key.
 *
 *  It is used to store, for each order, dishes selected by customer and their quantity.
 *  It is required because the customer might want more than one same dish.
 *
 *  Each DishOrderAssociation maintains the references to order and dish with a many-to-one DB constraint.
 */
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@IdClass(DishOrderAssociationKey.class)
public class DishOrderAssociation implements Serializable {

    @Id
    @ManyToOne(targetEntity= DishEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="dishId")
    private DishEntity dish;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @ManyToOne(targetEntity= OrderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="orderId")
    private OrderEntity order;

    private int quantity;


}
