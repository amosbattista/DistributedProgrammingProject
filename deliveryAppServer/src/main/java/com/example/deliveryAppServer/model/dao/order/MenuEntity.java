package com.example.deliveryAppServer.model.dao.order;

import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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

    public void removeDish(DishEntity dish){

        dishEntities.remove(dish);

    }

}
