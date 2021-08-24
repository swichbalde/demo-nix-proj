package com.example.demo.service;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.WeightRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class WeightService {

    private final WeightRepository weightRepository;

    public WeightService(WeightRepository weightRepository) {
        this.weightRepository = weightRepository;
    }

    public void saveWeightEntity(WeightModel weightEntity) {
        Long difference = weightEntity.getCurrentWeight() - weightEntity.getNewWeight();
        float bmi = (float) weightEntity.getNewWeight() / (weightEntity.getHeight() * weightEntity.getHeight());
        bmi *= 10000;
        weightRepository.save(new SaveWeightEntity(weightEntity.getCurrentWeight(), weightEntity.getNewWeight(), Instant.now(), difference, bmi));
    }

    public SaveWeightEntity getUserById(Long id) throws UserNotFoundException {
        Optional<SaveWeightEntity> currentEntity = weightRepository.findById(id);
        if (currentEntity.isEmpty())
            throw new UserNotFoundException("user with this id " + id + " not found");
        return currentEntity.get();
    }
}
