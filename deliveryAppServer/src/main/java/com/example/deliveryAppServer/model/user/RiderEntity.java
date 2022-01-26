package com.example.deliveryAppServer.model.user;

import lombok.Data;
import com.example.deliveryAppServer.model.order.OrderEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RiderEntity extends PersonEntity implements Serializable {

    @NotBlank
    private String documentPath;

    @NotBlank
    private String vehicleType;

    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "rider")
    private List<OrderEntity> orders;

}
