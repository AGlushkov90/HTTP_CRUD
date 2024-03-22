package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenGivenId_shouldReturnUser() {
        User user = new User(1L, "first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(userRepository.getByID(user.getId())).thenReturn(user);
        User userResult = userService.getByID(user.getId());
        assertEquals(user, userResult);
        verify(userRepository).getByID(user.getId());
    }

    @Test
    void whenGivenId_shouldReturnNull() {
        User user = new User(1L, "first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(userRepository.getByID(user.getId())).thenReturn(null);
        User userResult = userService.getByID(user.getId());
        assertNull(userResult);
        verify(userRepository).getByID(user.getId());
    }

    @Test
    void whenSaveLabel_shouldReturnLabel() throws IOException {
        User user = new User(1L, "first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        User created = userService.save(user);
        assertEquals(user, created);
        verify(userRepository).save(user);
    }

    @Test
    void shouldReturnAllUsers() throws JsonProcessingException {
        List<User> users = Arrays.asList(new User(1L, "first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE),
                new User(2L, "first",
                        Date.valueOf(LocalDate.now()), null, Status.ACTIVE));
        when(userRepository.getAll()).thenReturn(users);
        List<User> labelsResult = (List<User>) userService.getAll();
        assertEquals(users, labelsResult);
        verify(userRepository).getAll();
    }

    @Test
    void whenGivenId_shouldDeleteUser_ifFound() {
        User user = new User(1L, "first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(userRepository.delete(ArgumentMatchers.any(Long.class))).thenReturn(true);
        boolean deleted = userService.delete(user.getId());
        assertTrue(deleted);
        verify(userRepository).delete(user.getId());
    }

    @Test
    void edit() {
        User user = new User(1L, "first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        User newUser = new User(1L, "new first",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(userRepository.edit(ArgumentMatchers.any(User.class))).thenReturn(newUser);
        when(userRepository.getByID(user.getId())).thenReturn(user);
        User edited = userService.edit(user.getId(), newUser);
        assertEquals(user.getId(), edited.getId());
        assertEquals(newUser, edited);
    }
}