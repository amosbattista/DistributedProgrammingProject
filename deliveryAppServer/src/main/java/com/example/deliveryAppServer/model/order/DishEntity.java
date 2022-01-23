package com.example.deliveryAppServer.model.order;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "dish")
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
public class DishEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private ArrayList<String> ingredients;

    @NotBlank
    private double price;

    @ManyToOne(targetEntity=MenuEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="menuId")
    private MenuEntity menu;

    @ManyToMany(mappedBy = "dishList")
    List<OrderEntity> orderList;

}
