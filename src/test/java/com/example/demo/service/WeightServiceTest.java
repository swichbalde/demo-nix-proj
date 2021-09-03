package com.example.demo.service;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.exception.weight.WeightEntityNotFound;
import com.example.demo.repository.WeightRepository;
import com.example.demo.service.impl.WeightServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class WeightServiceTest {

    WeightService weightService;
    WeightRepository weightRepository;

    public WeightServiceTest() {
        weightRepository = mock(WeightRepository.class);
        weightService = new WeightServiceImpl(weightRepository);
    }

    @Test
    void testSaveEntity() {
        long id = 1;
        SaveWeightEntity saveWeightEntityOriginal = new SaveWeightEntity(9500L, 10000L, Instant.now(), 500L, 2379.5361f);

        WeightModel weightModel = new WeightModel(9500L, 10000L, 205L);

        when(weightRepository.save(notNull())).thenAnswer(invocation -> {
            SaveWeightEntity entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getCurrentWeight()).isEqualTo(saveWeightEntityOriginal.getCurrentWeight());
            assertThat(entity.getNewWeight()).isEqualTo(saveWeightEntityOriginal.getNewWeight());
            entity.setId(id);
            return entity;
        });

        SaveWeightEntity saveWeightEntitySaved = weightService.saveWeightEntity(weightModel);

        assertThat(weightModel.getCurrentWeight()).isEqualTo(saveWeightEntitySaved.getCurrentWeight());
        assertThat(weightModel.getNewWeight()).isEqualTo(saveWeightEntitySaved.getNewWeight());
    }

    @SneakyThrows
    @Test
    void testGetById() {
        long id = 1;
        long absId = 100;
        WeightModel weightModel = new WeightModel(9500L, 10000L, 205L);
        SaveWeightEntity saveWeightEntityOriginal = new SaveWeightEntity(9500L, 10000L, null, 500L, 2379.5361f);

        when(weightRepository.findById(id)).thenReturn(Optional.of(saveWeightEntityOriginal));
        SaveWeightEntity weightById = weightService.getWeightById(id);

        assertThatExceptionOfType(WeightEntityNotFound.class)
                .isThrownBy(() -> weightService.getWeightById(absId));

        assertThat(weightModel.getCurrentWeight()).isEqualTo(weightById.getCurrentWeight());
        assertThat(weightModel.getNewWeight()).isEqualTo(weightById.getNewWeight());
    }
}
