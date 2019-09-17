package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.Category;
import com.nastya.cookbook.model.Recipe;
import com.nastya.cookbook.model.User;
import com.nastya.cookbook.service.CategoryService;
import com.nastya.cookbook.service.RecipeService;
import com.nastya.cookbook.service.SecurityService;
import com.nastya.cookbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User userForm) {
        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/index"})
    public String welcome(ModelMap model, @RequestParam("page") Optional<Integer> page, @RequestParam("size")
            Optional<Integer> size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Recipe> recipes = recipeService.findAll();
        List<User> users = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        for (int i=0; i<recipes.size(); i++){
            users.add(i,userService.findById(recipes.get(i).getUser_id()).get());
            recipes.get(i).setUser(users.get(i));
            categories.add(i,categoryService.findById(recipes.get(i).getCategory_id()).get());
            recipes.get(i).setCategory(categories.get(i));
            if (recipes.get(i).getStatus()!=null&& recipes.get(i).getShort_link()!=null){
                recipes.remove(i);
                i--;
            }
        }

        final int currentPage = page.orElse(1);
        final int pageSize = size.orElse(20);
        Collections.reverse(recipes);
        Page<Recipe> recipePage = recipeService.findPaginated(PageRequest.of(currentPage - 1, pageSize), recipes);

        model.addAttribute("recipePage", recipePage);

        int totalPages = recipePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("username", userDetails.getUsername());
        if (recipes.size()==0){
            model.addAttribute("message",2);
        }
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

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("first_name", user.getFirst_name());
        model.addAttribute("last_name", user.getLast_name());

        return "user_details";
    }

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
