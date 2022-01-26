package com.example.deliveryAppServer.model.order;

import com.example.deliveryAppServer.model.user.ProviderEntity;
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
public class DishOrderAssociation implements Serializable {

    @Id
    @ManyToOne(targetEntity= DishEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="dishId")
    private DishEntity dish;

    @Id
    @ManyToOne(targetEntity= OrderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="orderId")
    private OrderEntity order;

    private int quantity;


}
