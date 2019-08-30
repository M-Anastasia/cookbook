package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.User;
import com.nastya.cookbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by fishn on 11.08.2019.
 */

//@Controller
public class WelcomeController {

//    @Autowired
//    private UserRepository userRepository;

//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private UserService userService;;
//
//    @Autowired
//    public WelcomeController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
//
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.userService = userService;
//    }
//
//    @GetMapping("/registration")
//    public String registration() {
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(User user, Model model) {
//        User ex_user = userService.findByUsername(user.getUsername());
////
//        if (ex_user != null) {
//            model.addAttribute("message", "User exists!");
//            return "registration";
//        }
//        model.addAttribute("name",user.getUsername());
//        userService.saveUser(user);
//        return "redirect:/index";
////        return "redirect:/login";
//    }
//
//    @GetMapping("/index")
//    public String greeting() {
//        return "index";
//    }

//    @GetMapping("/index")
//    public String root() {
//        return "index";
//    }

//    @GetMapping("/login")
//    public String login(Model model) {
//
//        return "index";
//    }
//
//    @GetMapping("/user")
//    public String userIndex() {
//        return "user/index";
//    }
}
