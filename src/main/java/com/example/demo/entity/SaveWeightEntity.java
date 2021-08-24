package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class SaveWeightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long currentWeight;
    private Long newWeight;
    private Instant firstCalc;
    private Long difference;
    private float bmi;

    public SaveWeightEntity() {
    }

    public SaveWeightEntity(Long currentWeight, Long newWeight, Instant firstCalc, Long difference, float bmi) {
        this.currentWeight = currentWeight;
        this.newWeight = newWeight;
        this.firstCalc = firstCalc;
        this.difference = difference;
        this.bmi = bmi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Long currentWeight) {
        this.currentWeight = currentWeight;
    }

    public Long getNewWeight() {
        return newWeight;
    }

    public void setNewWeight(Long newWeight) {
        this.newWeight = newWeight;
    }

    public Instant getFirstCalc() {
        return firstCalc;
    }

    public void setFirstCalc(Instant firstCalc) {
        this.firstCalc = firstCalc;
    }

    public Long getDifference() {
        return difference;
    }

    public void setDifference(Long difference) {
        this.difference = difference;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }
}
