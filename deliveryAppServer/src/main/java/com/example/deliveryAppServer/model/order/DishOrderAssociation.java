package com.example.deliveryAppServer.model.order;

import com.example.deliveryAppServer.model.user.ProviderEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

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
