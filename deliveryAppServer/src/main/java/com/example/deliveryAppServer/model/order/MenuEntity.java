package com.example.deliveryAppServer.model.order;

import lombok.Data;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
public class MenuEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(targetEntity=DishEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "menu")
    @NotBlank
    private List<DishEntity> dishEntities;

    @OneToOne(mappedBy = "menu")
    private ProviderEntity provider;

}
