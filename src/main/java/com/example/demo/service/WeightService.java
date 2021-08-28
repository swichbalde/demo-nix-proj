package com.example.demo.service;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.exception.UserNotFoundException;

public interface WeightService {

    void saveWeightEntity(WeightModel weightEntity);

    SaveWeightEntity getUserById(Long id) throws UserNotFoundException;
}
