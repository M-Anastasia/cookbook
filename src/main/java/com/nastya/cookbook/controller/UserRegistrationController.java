package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.Role;
import com.nastya.cookbook.model.User;
import com.nastya.cookbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

/**
 * Created by fishn on 15.08.2019.
 */

//@Controller
//@RequestMapping("/registration")
public class UserRegistrationController {
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/registration")
//    public String registration() {
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(User user, Model model) {
//        User userFromDb = userRepository.findByUsername(user.getUsername());
//
//        if (userFromDb != null ) {
//            model.addAttribute("message", "User exists!");
//            return "registration";
//        }
//
//        user.setRoles(Collections.singleton(Role.USER));
//        userRepository.save(user);
//
//        return "redirect:/login";
//    }
}
