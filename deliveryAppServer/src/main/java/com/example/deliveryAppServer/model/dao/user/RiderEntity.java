package com.example.deliveryAppServer.model.dao.user;

import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 *  It's a concrete entity of the system that will be stored persistently in DB.
 *  It represents a rider, who can deliver orders that have been accepted by the provider and are waiting for a rider.
 *
 *  Each rider must specify his vehicle type.
 *
 * Each rider maintains a list of own orders, for this reason a one-to-many DB constraint
 * is specified on 'orderList' field (each rider can have multiple orders, but one rider belongs to only one customer)
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RiderEntity extends PersonEntity implements Serializable {

    @NotBlank
    private String vehicleType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "rider")
    private List<OrderEntity> orders;

}
