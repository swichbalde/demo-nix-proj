package com.example.demo.entity.weight;

import lombok.Data;

@Data
public class WeightModel {
    private Long currentWeight;
    private Long newWeight;
    private Integer height;

    public WeightModel() {
    }

    public WeightModel(Long currentWeight, Long newWeight, Integer height) {
        this.currentWeight = currentWeight;
        this.newWeight = newWeight;
        this.height = height;
    }
}
