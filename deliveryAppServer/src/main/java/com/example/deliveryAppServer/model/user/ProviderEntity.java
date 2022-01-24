package com.example.deliveryAppServer.model.user;

import com.example.deliveryAppServer.model.order.MenuEntity;
import lombok.Data;
import com.example.deliveryAppServer.model.order.OrderEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
public class ProviderEntity extends PersonEntity implements Serializable {

    @NotBlank
    private String providerName;

    @NotBlank
    private String cuisine;

    @NotBlank
    private String address;

    @NotNull
    private Boolean doDelivering;

    @NotNull
    private Boolean doTakeAway;

    @NotNull
    private Boolean hasOwnRiders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private MenuEntity menu;

    @NotNull
    private Boolean isAvailable;

    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "provider")
    private List<OrderEntity> orderList;


}
