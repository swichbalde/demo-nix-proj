package com.example.demo.entity;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public SaveWeightEntity(Long currentWeight, Long newWeight, Instant firstCalc, Long difference, float bmi, User user) {
        this.currentWeight = currentWeight;
        this.newWeight = newWeight;
        this.firstCalc = firstCalc;
        this.difference = difference;
        this.bmi = bmi;
        this.user = user;
    }
}
