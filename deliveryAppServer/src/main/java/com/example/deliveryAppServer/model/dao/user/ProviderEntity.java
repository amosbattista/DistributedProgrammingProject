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
    private String cuisine;

    @NotBlank
    private String address;

    @NotNull
    private Boolean doDelivering;

    @NotNull
    private Boolean doTakeAway;

    @NotNull
    private Boolean hasOwnRiders;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private MenuEntity menu;

    @NotNull
    private Boolean isAvailable;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "provider")
    private List<OrderEntity> orderList;


}
