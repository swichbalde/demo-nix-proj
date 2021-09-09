package com.example.demo.service;

import com.example.demo.entity.SaveWeightEntity;
import com.example.demo.entity.model.WeightModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.weight.WeightEntityNotFound;
import com.example.demo.repository.WeightRepository;
import com.example.demo.service.impl.WeightServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class WeightServiceTest {

    WeightService weightService;
    WeightRepository weightRepository;
    UserService userService;

    public WeightServiceTest() {
        weightRepository = mock(WeightRepository.class);
        userService = mock(UserService.class);
        weightService = new WeightServiceImpl(weightRepository, userService);
    }

    @Test
    void testSaveEntity() throws Exception {
        long id = 1;
        SaveWeightEntity saveWeightEntityOriginal = new SaveWeightEntity(9500L, 10000L, Instant.now(), 500L, 2379.5361f, null);

        WeightModel weightModel = new WeightModel(9500L, 10000L, 205L);

        when(weightRepository.save(notNull())).thenAnswer(invocation -> {
            SaveWeightEntity entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getCurrentWeight()).isEqualTo(saveWeightEntityOriginal.getCurrentWeight());
            assertThat(entity.getNewWeight()).isEqualTo(saveWeightEntityOriginal.getNewWeight());
            entity.setId(id);
            return entity;
        });
        when(userService.findById(id)).thenReturn(new User());

        SaveWeightEntity saveWeightEntitySaved = weightService.saveWeightEntity(weightModel, id);

        assertThat(weightModel.getCurrentWeight()).isEqualTo(saveWeightEntitySaved.getCurrentWeight());
        assertThat(weightModel.getNewWeight()).isEqualTo(saveWeightEntitySaved.getNewWeight());
    }

    @Test
    void testGetById() throws Exception {
        long id = 1;
        long absId = 100;
        WeightModel weightModel = new WeightModel(9500L, 10000L, 205L);
        SaveWeightEntity saveWeightEntityOriginal = new SaveWeightEntity(9500L, 10000L, null, 500L, 2379.5361f, null);

        when(weightRepository.findById(id)).thenReturn(Optional.of(saveWeightEntityOriginal));
        SaveWeightEntity weightById = weightService.getWeightById(id);

        assertThatExceptionOfType(WeightEntityNotFound.class)
                .isThrownBy(() -> weightService.getWeightById(absId));

        assertThat(weightModel.getCurrentWeight()).isEqualTo(weightById.getCurrentWeight());
        assertThat(weightModel.getNewWeight()).isEqualTo(weightById.getNewWeight());
    }
}
