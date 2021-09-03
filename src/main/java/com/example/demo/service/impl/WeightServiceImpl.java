package com.example.demo.service.impl;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.exception.weight.WeightEntityNotFound;
import com.example.demo.repository.WeightRepository;
import com.example.demo.service.WeightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class WeightServiceImpl implements WeightService {

    private final WeightRepository weightRepository;

    public WeightServiceImpl(WeightRepository weightRepository) {
        this.weightRepository = weightRepository;
    }

    public SaveWeightEntity saveWeightEntity(WeightModel weightEntity) {
        Long difference = weightEntity.getCurrentWeight() - weightEntity.getNewWeight();
        float bmi = (float) weightEntity.getNewWeight() / (weightEntity.getHeight() * weightEntity.getHeight());
        bmi *= 10000;
        log.info("IN saveWeightEntity, weightEntity successfully saved");
        return weightRepository.save(new SaveWeightEntity(weightEntity.getCurrentWeight(), weightEntity.getNewWeight(), Instant.now(), difference, bmi));
    }

    public SaveWeightEntity getWeightById(Long id) throws WeightEntityNotFound {
        Optional<SaveWeightEntity> currentEntity = weightRepository.findById(id);
        if (currentEntity.isEmpty()) {
            log.warn("IN getWeightById weight entity with this id {} not found", id);
            throw new WeightEntityNotFound("user with this id " + id + " not found");
        }
        log.info("IN getWeightById weight entity successfully found by id: {}", id);
        return currentEntity.get();
    }
}
