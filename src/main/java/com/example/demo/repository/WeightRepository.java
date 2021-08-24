package com.example.demo.repository;

import com.example.demo.entity.SaveWeightEntity;
import org.springframework.data.repository.CrudRepository;

public interface WeightRepository extends CrudRepository<SaveWeightEntity, Long> {
}
