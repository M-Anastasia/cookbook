package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.Category;
import com.nastya.cookbook.model.Recipe;
import com.nastya.cookbook.model.User;
import com.nastya.cookbook.service.CategoryService;
import com.nastya.cookbook.service.RecipeService;
import com.nastya.cookbook.service.SecurityService;
import com.nastya.cookbook.service.UserService;
import com.nastya.cookbook.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishn on 16.08.2019.
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/registration")
    public String registration(Model model) {
//        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User userForm) {
//        userValidator.validate(userForm, bindingResult);

//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
//        if (error != null)
//            model.addAttribute("error", "Your username and password is invalid.");
//
//        if (logout != null)
//            model.addAttribute("message", "You have been logged out successfully.");


        return "login";
    }

    @GetMapping({"/", "/index"})
    public String welcome(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Recipe> recipes = recipeService.findAll();

        List<String> categories = new ArrayList<>();
        for (int i=0; i<recipes.size(); i++){
            categories.add(categoryService.findById(recipes.get(i).getCategory_id()).get().getName());
            recipes.get(i).setCategory(categoryService.findByName(categories.get(i)));
            if (recipes.get(i).getStatus()!=null&& recipes.get(i).getShort_link()!=null){
                recipes.remove(i);
                i--;
            }
        }

//        model.addAttribute("categories",categories);
        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());
        return "index";
    }

    @GetMapping({"/user_details"})
    public String userDetails(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User user = userService.findByUsername(userDetails.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("first_name", user.getFirst_name());
        model.addAttribute("last_name", user.getLast_name());

        return "user_details";
    }

//    @PostMapping({"/user_details"})
    @RequestMapping(value="/user_details",params="edit",method= RequestMethod.POST)
    public String editUser(String email, String first_name, String last_name, ModelMap model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User user = userService.findByUsername(userDetails.getUsername());

        if (email!=null && !email.isEmpty()){
            user.setEmail(email);
        }
        if (first_name!=null && !first_name.isEmpty()){
            user.setFirst_name(first_name);
        }
        if (last_name!=null && !last_name.isEmpty()){
            user.setLast_name(last_name);
        }
        userService.update(user);

//        Long id = userService.findByUsername(userDetails.getUsername()).getId();

//        userService.setFixedEmailFor(email,id);
//        userService.setFixedFirstnameFor(first_name,id);
//        userService.setFixedLastnameFor(last_name,id);

//        userService.flush();

//        Authentication request = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword());
//        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(request));

//        User user = userService.findByUsername(userDetails.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("first_name", user.getFirst_name());
        model.addAttribute("last_name", user.getLast_name());

        return "user_details";
    }


//    @PostMapping({"/user_details"})
    @RequestMapping(value="/user_details",params="change_password",method= RequestMethod.POST)
    public String updatePassword(String password, String new_password, String confirm_password, String username, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = userService.findByUsername(userDetails.getUsername());

        if (encoder.matches(password, user.getPassword()) && new_password.equals(confirm_password)){
            User new_user = userService.findByUsername(username);
            new_user.setPassword(encoder.encode(new_password));
            userService.update(new_user);
            model.addAttribute("message",1);
        } else {
            model.addAttribute("message",2);
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("first_name", user.getFirst_name());
        model.addAttribute("last_name", user.getLast_name());

        return "user_details";
    }
}
