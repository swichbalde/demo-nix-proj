package com.example.demo.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SaveWeightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long currentWeight;
    private Long newWeight;
    private Instant firstCalc;
    private Long difference;
    private float bmi;

    public SaveWeightEntity(Long currentWeight, Long newWeight, Instant firstCalc, Long difference, float bmi) {
        this.currentWeight = currentWeight;
        this.newWeight = newWeight;
        this.firstCalc = firstCalc;
        this.difference = difference;
        this.bmi = bmi;
    }

}
