package com.nastya.cookbook.service;

import com.nastya.cookbook.model.User;

import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    Optional<User> findById(Long id);

    void save(User user);

    void update(User user);
}
