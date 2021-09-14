package com.example.demo.entity.model;

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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
