package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Role;
import com.nastya.cookbook.model.User;
import com.nastya.cookbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by fishn on 16.08.2019.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

//    @Override
//    public void changePassword(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }

    //    @Override
//    public void setFixedUsernameFor(String username, Long id) {
//        userRepository.setFixedUsernameFor(username,id);
//    }
//
//    @Override
//    public void setFixedEmailFor(String email, Long id) {
//        if (!email.isEmpty()){
//            userRepository.setFixedEmailFor(email,id);
//        }
//    }
//
//    @Override
//    public void setFixedFirstnameFor(String first_name, Long id) {
//        if (!first_name.isEmpty()){
//            userRepository.setFixedFirstnameFor(first_name,id);
//        }
//    }
//
//    @Override
//    public void setFixedLastnameFor(String last_name, Long id) {
//        if (!last_name.isEmpty()){
//            userRepository.setFixedLastnameFor(last_name,id);
//        }
//    }
//
//    @Override
//    public void flush() {
//        userRepository.flush();
//    }
}
