package com.example.deliveryAppServer.model.dao.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "dish")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) //Non necessario
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"name", "menuId"})
})
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
    @ManyToOne(targetEntity = MenuEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId")
    private MenuEntity menu;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(targetEntity = DishOrderAssociation.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "dish")
    private List<DishOrderAssociation> dishOrderAssociations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishEntity that = (DishEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
