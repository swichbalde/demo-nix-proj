package com.example.demo.repository;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeightRepository extends JpaRepository<SaveWeightEntity, Long> {

    Optional<SaveWeightEntity> findByUser(User user);
}
