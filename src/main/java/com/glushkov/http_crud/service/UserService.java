package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glushkov.http_crud.model.User;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.UserRepository;
import com.glushkov.http_crud.repository.impl.ORMUserRepositoryImpl;

import java.io.IOException;
import java.util.List;


public class UserService {
    public UserRepository userRepository = new ORMUserRepositoryImpl();
    public UserService() {
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByID(Long id){
        return userRepository.getByID(id);
    }

    public List<User> getAll() throws JsonProcessingException {
        return userRepository.getAll();
    }

    public Boolean delete(Long id) {
        return userRepository.delete(id);
    }

    public User save(User user) throws IOException {
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }


    public User edit(Long id, User userJson) {
        User user = userRepository.getByID(id);
        user.setName(userJson.getName());
        return userRepository.edit(user);
    }


}


