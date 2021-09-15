package com.example.demo.service;

import com.example.demo.entity.model.ResponseUserAdmin;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.Status;
import com.example.demo.entity.user.User;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UserPasswordSmall;
import com.example.demo.repository.user.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    UserService userService;
    UserRepository userRepository;
    RoleRepository roleRepository;
    BCryptPasswordEncoder passwordEncoder;

    public UserServiceTest() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        this.userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void registrationTest() throws Exception {
        Long id = 1L;

        User userSmall = new User(id, "test", "test");
        User user = new User(id, "test", "testtest");
        Role role = new Role();
        role.setName("ROLE_USER");


        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        User registration = userService.registration(user);

        assertThatExceptionOfType(UserPasswordSmall.class)
                .isThrownBy(() -> userService.registration(userSmall));

        assertThat(registration.getId()).isEqualTo(id);
        assertThat(registration.getLogin()).isEqualTo(user.getLogin());
        assertThat(registration.getRoles().get(0)).isEqualTo(role);
        assertThat(registration.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void registrationAdminTest() throws Exception {
        Long id = 1L;

        User user = new User(id, "test", "testtest");
        User userSmall = new User(id, "test", "test");
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        User registration = userService.registrationAdmin(user);

        assertThatExceptionOfType(UserPasswordSmall.class)
                .isThrownBy(() -> userService.registration(userSmall));

        assertThat(registration.getId()).isEqualTo(id);
        assertThat(registration.getLogin()).isEqualTo(user.getLogin());
        assertThat(registration.getRoles().get(0)).isEqualTo(role);
        assertThat(registration.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void getAllTest() throws Exception {
        List<ResponseUserAdmin> all = userService.getAll();
        assertThat(all.size()).isEqualTo(0);

        Long id = 1L;

        User user = new User(id, "test", "testtest");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.registration(user);

        all = userService.getAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void findByIdTest() throws Exception{
        Long id = 1L;

        User user = new User(id, "test", "testtest");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.registration(user);

        User byId = userService.findById(id);

        assertThat(byId.getId()).isEqualTo(id);
        assertThat(byId.getLogin()).isEqualTo(user.getLogin());
        assertThat(byId.getRoles().get(0)).isEqualTo(role);
        assertThat(byId.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void findByLoginTest() throws Exception{
        Long id = 1L;
        String login = "test";

        User user = new User(id, login, "testtest");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByLogin(login)).thenReturn(user);

        userService.registration(user);

        User byId = userService.findByLogin(login);

        assertThat(byId.getId()).isEqualTo(id);
        assertThat(byId.getLogin()).isEqualTo(user.getLogin());
        assertThat(byId.getRoles().get(0)).isEqualTo(role);
        assertThat(byId.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void deleteByIdTest() throws Exception{
        Long id = 1L;
        Long absId = 100L;
        String login = "test";

        User user = new User(id, login, "testtest");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findById(absId)).thenReturn(Optional.empty());

        userService.deleteById(id);

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.deleteById(absId));
    }

    @Test
    void updateUserTest() throws Exception{
        Long id = 1L;
        Long absId = 100L;
        String login = "test";

        User user = new User(id, login, "testtest");
        User absUser = new User(absId, login, "testtest");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findById(absId)).thenReturn(Optional.empty());

        userService.updateUser(user);

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.updateUser(absUser));
    }
}
