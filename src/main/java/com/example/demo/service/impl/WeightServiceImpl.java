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

    public SaveWeightEntity saveWeightEntity(WeightModel weightEntity, Long UserId) throws UserNotFoundException {
        User user = userService.findById(UserId);
        if (user == null) {
            log.warn("IN saveWeightEntity user with id: {} not found", UserId);
            throw new UserNotFoundException("IN saveWeightEntity user with id " + UserId + " not found");
        }

        long difference = weightEntity.getCurrentWeight() - weightEntity.getNewWeight();
        float bmi = (float) weightEntity.getNewWeight() / (weightEntity.getHeight() * weightEntity.getHeight());
        bmi *= 10000;

        SaveWeightEntity saveWeightEntity = weightRepository.save(new SaveWeightEntity(weightEntity.getCurrentWeight(), weightEntity.getNewWeight(), Instant.now(), difference, bmi, user));
        user.setSaveWeightEntity(saveWeightEntity);
        userService.updateUser(user);

        log.info("IN saveWeightEntity, weightEntity successfully saved");
        return saveWeightEntity;
    }

    public SaveWeightEntity getWeightByUserId(Long id) throws UserNotFoundException, WeightEntityNotFound {
        User user = userService.findById(id);
        if (user == null) {
            log.warn("IN getWeightByUserId user entity with this id {} not found", id);
            throw new UserNotFoundException("user with this id " + id + " not found");
        }
        Optional<SaveWeightEntity> currentEntity = weightRepository.findByUser(user);
        if (currentEntity.isEmpty()) {
            log.warn("IN getWeightByUserId weight entity with this id {} not found", id);
            throw new WeightEntityNotFound("user with this id " + id + " not found");
        }
        log.info("IN getWeightByUserId weight entity successfully found by id: {}", id);
        return currentEntity.get();
    }

    public SaveWeightEntity updateWeightEntity(WeightModel weightModel, Long id) throws WeightEntityNotFound {
        SaveWeightEntity saveWeightEntity = weightRepository.findById(id).orElseThrow(() -> {
            log.warn("IN updateWeightEntity weight entity with this id {} not found", id);
            return new WeightEntityNotFound("weight entity with this id " + id + " not found");
        });

        if (weightModel.getCurrentWeight() == null)
            saveWeightEntity.setCurrentWeight(weightModel.getCurrentWeight());

        if (weightModel.getNewWeight() == null)
            saveWeightEntity.setNewWeight(weightModel.getNewWeight());

        long difference = weightModel.getCurrentWeight() - weightModel.getNewWeight();
        float bmi = (float) weightModel.getNewWeight() / (weightModel.getHeight() * weightModel.getHeight());
        bmi *= 10000;

        saveWeightEntity.setDifference(difference);
        saveWeightEntity.setBmi(bmi);

        log.info("IN updateWeightEntity weight entity successfully updated by id: {}", id);
        return weightRepository.save(saveWeightEntity);
    }
}
