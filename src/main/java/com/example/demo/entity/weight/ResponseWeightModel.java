package com.example.demo.entity.weight;

import lombok.Data;

import java.time.Instant;

@Data
public class ResponseWeightModel {

    private Long currentWeight;
    private Long newWeight;
    private Instant firstCalc;
    private Long difference;
    private float bmi;

    public ResponseWeightModel() {
    }

    public ResponseWeightModel(Long currentWeight, Long newWeight, Instant firstCalc, Long difference, float bmi) {
        this.currentWeight = currentWeight;
        this.newWeight = newWeight;
        this.firstCalc = firstCalc;
        this.difference = difference;
        this.bmi = bmi;
    }
}
