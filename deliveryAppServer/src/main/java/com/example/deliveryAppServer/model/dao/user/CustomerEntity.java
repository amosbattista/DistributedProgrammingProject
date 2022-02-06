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
 *  It represents a customer, who can place orders on the app, selecting dishes from providers menu.
 *
 *  Each customer must specify his address to ensure that a rider can deliver the order.
 *
 * Each customer maintains a list of own orders, for this reason a one-to-many DB constraint
 * is specified on 'orderList' field (each customer can have multiple orders, but one order belongs to only one customer)
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CustomerEntity extends PersonEntity implements Serializable {

    @NotBlank
    private String address;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "customer")
    private List<OrderEntity> orderList;






}
