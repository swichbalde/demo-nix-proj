package com.example.demo.service.impl;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.weight.WeightEntityNotFound;
import com.example.demo.repository.WeightRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.WeightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class WeightServiceImpl implements WeightService {

    private final WeightRepository weightRepository;
    private final UserService userService;

    public WeightServiceImpl(WeightRepository weightRepository, UserService userService) {
        this.weightRepository = weightRepository;
        this.userService = userService;
    }

    public SaveWeightEntity saveWeightEntity(WeightModel weightEntity, Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        if (user == null) {
            log.warn("IN saveWeightEntity user with id: {} not found", id);
            throw new UserNotFoundException("IN saveWeightEntity user with id " + id + " not found");
        }

        Long difference = weightEntity.getCurrentWeight() - weightEntity.getNewWeight();
        float bmi = (float) weightEntity.getNewWeight() / (weightEntity.getHeight() * weightEntity.getHeight());
        bmi *= 10000;
        log.info("IN saveWeightEntity, weightEntity successfully saved");
        return weightRepository.save(new SaveWeightEntity(weightEntity.getCurrentWeight(), weightEntity.getNewWeight(), Instant.now(), difference, bmi, user));
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
