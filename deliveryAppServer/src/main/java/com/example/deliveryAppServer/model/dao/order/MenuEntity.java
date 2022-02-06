package com.example.deliveryAppServer.model.dao.order;

import com.example.deliveryAppServer.model.dao.user.ProviderEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *  It's a concrete entity of the system that will be stored persistently in DB.
 *  It represents the provider menu. The 'id' field represents its primary key in the DB.
 *
 * Each menu maintains a list of own dishes, for this reason a one-to-many DB constraint
 * is specified on 'dishList' field (each menu can have multiple dishes, but one dish belongs to only one menu)
 *
 * Each menu maintains the reference to the provider, with a one-to-one DB constraint (each menu has one provider,
 * and one provider has one menu)
 */
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) //Non necessario
public class MenuEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(targetEntity=DishEntity.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "menu")
    private List<DishEntity> dishList;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(mappedBy = "menu")
    private ProviderEntity provider;

    public void addDish(DishEntity dish){

        dishList.add(dish);

    }

    public void removeDish(DishEntity dish){

        dishList.remove(dish);

    }

}
