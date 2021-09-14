package com.example.demo.controller;

import com.example.demo.entity.weight.WeightModel;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.weight.WeightEntityNotFound;
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
    public ResponseEntity saveWeight(@RequestBody WeightModel weightEntity, @PathVariable Long id) {
        try {
            weightService.saveWeightEntity(weightEntity, id);
            return ResponseEntity.ok(weightEntity);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getWeightByUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(weightService.getWeightByUserId(id));
        } catch (UserNotFoundException | WeightEntityNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateWeightEntity(@RequestBody WeightModel weightEntity, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(weightService.updateWeightEntity(weightEntity, id));
        } catch (WeightEntityNotFound ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }
}
