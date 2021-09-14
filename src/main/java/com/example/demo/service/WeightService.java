package com.example.demo.service;

import com.example.demo.entity.weight.ResponseWeightModel;
import com.example.demo.entity.weight.SaveWeightEntity;
import com.example.demo.entity.weight.WeightModel;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.weight.WeightEntityNotFound;

public interface WeightService {

    SaveWeightEntity saveWeightEntity(WeightModel weightEntity, Long id) throws UserNotFoundException;

    ResponseWeightModel getWeightByUserId(Long id) throws WeightEntityNotFound, UserNotFoundException;

    SaveWeightEntity updateWeightEntity(WeightModel weightModel, Long id) throws WeightEntityNotFound;
}
