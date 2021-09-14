package com.example.demo.service;

import com.example.demo.entity.weight.ResponseWeightModel;
import com.example.demo.entity.weight.SaveWeightEntity;
import com.example.demo.entity.weight.WeightModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.weight.WeightEntityNotFound;
import com.example.demo.repository.WeightRepository;
import com.example.demo.service.impl.WeightServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
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

        WeightModel weightModel = new WeightModel(9500L, 10000L, 205);

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
        User user = new User(id, "test", "test");
        WeightModel weightModel = new WeightModel(9500L, 10000L, 205);
        SaveWeightEntity saveWeightEntityOriginal =
                new SaveWeightEntity(9500L, 10000L, null, 500L, 2379.5361f, user);

        when(userService.findById(id)).thenReturn(user);
        when(weightRepository.findAllByUser(user)).thenReturn(List.of(saveWeightEntityOriginal));
        ResponseWeightModel weightByUserId = weightService.getWeightByUserId(id);

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> weightService.getWeightByUserId(absId));

        assertThat(weightModel.getCurrentWeight()).isEqualTo(weightByUserId.getCurrentWeight());
        assertThat(weightModel.getNewWeight()).isEqualTo(weightByUserId.getNewWeight());
    }

    @Test
    void testUpdateWeight() throws Exception {
        var presentId = 1L;
        var absentId = 10L;
        var update = new WeightModel();
        update.setCurrentWeight(9500L);
        update.setNewWeight(10000L);
        update.setHeight(205);

        SaveWeightEntity saveWeightEntityOriginal =
                new SaveWeightEntity(9500L, 10000L, null, 500L, 2379.5361f, null);

        when(weightRepository.findById(absentId)).thenReturn(Optional.empty());
        when(weightRepository.findById(presentId)).thenReturn(Optional.of(saveWeightEntityOriginal));
        when(weightRepository.save(same(saveWeightEntityOriginal))).thenReturn(saveWeightEntityOriginal);

        assertThatExceptionOfType(WeightEntityNotFound.class)
                .isThrownBy(() -> weightService.updateWeightEntity(update, absentId));

        verify(weightRepository).findById(absentId);

        SaveWeightEntity saveWeightEntity = weightService.updateWeightEntity(update, presentId);

        assertThat(update.getCurrentWeight()).isEqualTo(saveWeightEntity.getCurrentWeight());
        assertThat(update.getNewWeight()).isEqualTo(saveWeightEntity.getNewWeight());
        verify(weightRepository).findById(presentId);
        verify(weightRepository).save(saveWeightEntityOriginal);

        verifyNoMoreInteractions(weightRepository);
    }
}
