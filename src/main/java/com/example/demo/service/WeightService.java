package com.example.demo.service;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.weight.WeightEntityNotFound;

public interface WeightService {

    SaveWeightEntity saveWeightEntity(WeightModel weightEntity, Long id) throws UserNotFoundException;

    SaveWeightEntity getWeightByUserId(Long id) throws WeightEntityNotFound, UserNotFoundException;

    SaveWeightEntity updateWeightEntity(WeightModel weightModel, Long id) throws WeightEntityNotFound;
}
