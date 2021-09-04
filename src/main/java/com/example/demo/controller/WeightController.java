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

    @PostMapping("/{id}")
    public ResponseEntity control(@RequestBody WeightModel weightEntity, @PathVariable String id) {
        try {
            weightService.saveWeightEntity(weightEntity, Long.valueOf(id));
            return ResponseEntity.ok(weightEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(weightService.getWeightById(Long.valueOf(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
