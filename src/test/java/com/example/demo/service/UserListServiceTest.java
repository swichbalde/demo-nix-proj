package com.example.demo.service;

import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.repository.UserListRepository;
import com.example.demo.service.impl.UserListServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

public class UserListServiceTest {

    UserListService userListService;

    UserListRepository userListRepository;
    UserService userService;

    @BeforeEach
    void setUp() {
        userListRepository = mock(UserListRepository.class);
        userService = mock(UserService.class);
        userListService = new UserListServiceImpl(userListRepository, userService);
    }

    @SneakyThrows
    @Test
    void testUserListById() {
        var absentId = 125L;
        var presentId = 1L;
        var userList = new UserListEntity();
        userList.setId(presentId);
        userList.setRecommendList("aaa");
        userList.setBanList("bbb");

        when(userListRepository.findById(absentId)).thenReturn(Optional.empty());
        when(userListRepository.findById(presentId)).thenReturn(Optional.of(userList));

        assertThatExceptionOfType(UserListNotFoundException.class)
                .isThrownBy(() -> userListService.getUserListById(absentId))
                .satisfies(e -> assertThat(e.getMessage()).isEqualTo("user list not found by this id : " + absentId));

        UserListEntity userListEntityPrs = userListService.getUserListById(presentId);
        assertThat(userListEntityPrs).isEqualTo(userList);

        verify(userListRepository).findById(absentId);


        verify(userListRepository).findById(presentId);

        verifyNoMoreInteractions(userListRepository);
    }

    @SneakyThrows
    @Test
    void testSaveUserList() {
        long id = 1;
        var userList = new UserListEntity();
        userList.setId(id);
        userList.setRecommendList("aaa");
        userList.setBanList("bbb");

        when(userListRepository.save(notNull())).thenAnswer(invocation -> {
            UserListEntity entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNotNull();
            assertThat(entity.getRecommendList()).isEqualTo(userList.getRecommendList());
            assertThat(entity.getBanList()).isEqualTo(userList.getBanList());
            entity.setId(id);
            return entity;
        });

        UserListEntity userListEntity = userListService.saveUserList(userList, id);

        assertThat(userListEntity.getId()).isEqualTo(id);
        assertThat(userListEntity.getRecommendList()).isEqualTo(userList.getRecommendList());
        assertThat(userListEntity.getBanList()).isEqualTo(userList.getBanList());

        verify(userService).findById(id);
        verify(userListRepository).save(userList);
    }

    @SneakyThrows
    @Test
    void testUpdateUserList() {
        var presentId = 1L;
        var absentId = 10L;
        var update = new UserListEntity();
        update.setRecommendList("new aaa");
        update.setBanList("new bbb");
        update.setFilter("filter");

        var userList = new UserListEntity();
        userList.setId(presentId);
        userList.setRecommendList("aaa");
        userList.setBanList("bbb");

        when(userListRepository.findById(absentId)).thenReturn(Optional.empty());
        when(userListRepository.findById(presentId)).thenReturn(Optional.of(userList));
        when(userListRepository.save(same(userList))).thenReturn(userList);

        assertThatExceptionOfType(UserListNotFoundException.class)
                .isThrownBy(() -> userListService.updateUserListById(absentId, update))
                .satisfies(e -> assertThat(e.getMessage()).isEqualTo("user list not found by this id : " + absentId));

        verify(userListRepository).findById(absentId);

        userListService.updateUserListById(presentId, update);

        assertThat(userList.getRecommendList()).isEqualTo(update.getRecommendList());
        assertThat(userList.getBanList()).isEqualTo(update.getBanList());
        verify(userListRepository).findById(presentId);
        verify(userListRepository).save(userList);

        verifyNoMoreInteractions(userListRepository);
    }
}
