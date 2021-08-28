package com.example.demo.controller;

import com.example.demo.entity.model.WeightModel;
import com.example.demo.service.impl.WeightServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weight")
public class WeightController {

    private final WeightServiceImpl weightService;

    public WeightController(WeightServiceImpl weightService) {
        this.weightService = weightService;
    }

    @PostMapping("/control")
    public ResponseEntity<String> control(@RequestBody WeightModel weightEntity) {
        try {
            weightService.saveWeightEntity(weightEntity);
            return ResponseEntity.ok("weight entity created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(weightService.getUserById(Long.valueOf(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
