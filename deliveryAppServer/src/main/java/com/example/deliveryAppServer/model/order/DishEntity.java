package com.example.deliveryAppServer.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "dish")
@Data
@EntityListeners(AuditingEntityListener.class) //Non necessario
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class DishEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @ElementCollection
    private List<String> ingredients;

    @NotNull
    private double price;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity=MenuEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="menuId")
    private MenuEntity menu;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity=DishOrderAssociation.class,cascade = CascadeType.MERGE , fetch = FetchType.LAZY, mappedBy = "dish")
    private List<DishOrderAssociation> dishOrderAssociations;

}
