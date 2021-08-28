package com.example.demo.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String recipe_name;

    private String ingredients;
    private Integer cook_time;
    private Integer calories;
    private Integer cost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipeEntity that = (RecipeEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 738380931;
    }
}
