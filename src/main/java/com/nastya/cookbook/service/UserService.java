package com.nastya.cookbook.service;

import com.nastya.cookbook.model.User;

import java.util.Optional;

/**
 * Created by fishn on 15.08.2019.
 */

public interface UserService {

    User findByUsername(String username);
    Optional<User> findById(Long id);
    void save(User user);
    void update(User user);
//    void changePassword(User user);
//    void setFixedUsernameFor(String username, Long id);
//    void setFixedEmailFor(String email, Long id);
//    void setFixedFirstnameFor(String first_name, Long id);
//    void setFixedLastnameFor(String last_name, Long id);
//    void flush();
}
