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

@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@DynamicUpdate
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RiderEntity extends PersonEntity implements Serializable {

    @NotBlank
    private String documentPath;

    @NotBlank
    private String vehicleType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity= OrderEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "rider")
    private List<OrderEntity> orders;

}
