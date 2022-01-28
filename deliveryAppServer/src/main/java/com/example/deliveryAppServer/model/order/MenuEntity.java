package com.example.deliveryAppServer.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.example.deliveryAppServer.model.user.ProviderEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) //Non necessario
public class MenuEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(targetEntity=DishEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "menu")
    private List<DishEntity> dishEntities;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(mappedBy = "menu")
    private ProviderEntity provider;

    public void addDish(DishEntity dish){

        dishEntities.add(dish);

    }

}
