package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.Category;
import com.nastya.cookbook.model.Recipe;
import com.nastya.cookbook.model.Share;
import com.nastya.cookbook.service.CategoryService;
import com.nastya.cookbook.service.RecipeService;
import com.nastya.cookbook.service.ShareService;
import com.nastya.cookbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by fishn on 21.08.2019.
 */
@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShareService shareService;

    @GetMapping("/add_recipe")
    public String registration(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Category> categories = categoryService.findAll();

//        model.addAttribute("categ", new Category());
        model.addAttribute("categories", categories);
        model.addAttribute("username", userDetails.getUsername());
        return "add_recipe";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipe(@PathVariable("id") Long id, ModelMap model){

        Recipe recipeForm = recipeService.findById(id).get();

        model.addAttribute("path",recipeForm.getImage_path());
        model.addAttribute("name",recipeForm.getName());
        model.addAttribute("date",recipeForm.getCreation_date());
        model.addAttribute("category","Desert");
        model.addAttribute("description",recipeForm.getDescription());
        return "recipe";
    }

    @PostMapping({"/add_recipe"})
//    @RequestMapping(value="/user_details",params="edit",method= RequestMethod.POST)
    public String createRecipe(Recipe recipeForm, MultipartFile file, Long categ, ModelMap model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        recipeForm.setImage_path(recipeService.saveFile(file));

        recipeForm.setCategory_id(categ);



        recipeForm.setUser_id(userService.findByUsername(userDetails.getUsername()).getId());
//        recipeForm.setCategory_id(new Long(1));
        Date date = new Date();
        recipeForm.setCreation_date(date);

        if (recipeForm.getStatus()!=null && recipeForm.getStatus().equals("on")){
            recipeForm.setShort_link(recipeService.generateShortLink());
        }

        String category = categoryService.findById(categ).get().getName();

        recipeService.save(recipeForm);

        model.addAttribute("path",recipeForm.getImage_path());
        model.addAttribute("name",recipeForm.getName());
        model.addAttribute("date",recipeForm.getCreation_date());
        model.addAttribute("category",category);
        model.addAttribute("description",recipeForm.getDescription());

        model.addAttribute("username", userDetails.getUsername());
        Long id = recipeService.findByName(recipeForm.getName()).getId();
        return "redirect:/recipe/"+id;
    }

    @GetMapping({"/my_recipes"})
    public String myRecipes(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Recipe> recipes = recipeService.findByUser_id(userService.findByUsername(userDetails.getUsername()).getId());

        List<String> categories = new ArrayList<>();
//        categories.add(categoryService.findById(recipes.getCategory_id()).get().getName());
        for (int i=0; i<recipes.size(); i++){
            categories.add(categoryService.findById(recipes.get(i).getCategory_id()).get().getName());
            recipes.get(i).setCategory(categoryService.findByName(categories.get(i)));
        }

//        model.addAttribute("categories",categories);
        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());
        return "my_recipes";
    }

    @PostMapping({"/my_recipes"})
    public String deleteRecipe(ModelMap model, Long recipe_id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Recipe> recipes = recipeService.findByUser_id(userService.findByUsername(userDetails.getUsername()).getId());
        for (int i=0; i<recipes.size(); i++){
            if (recipes.get(i).getId().equals(recipe_id)){
                recipeService.delete(recipes.get(i));
                recipes.remove(i);
                break;
            }
        }

        List<String> categories = new ArrayList<>();
        for (int i=0; i<recipes.size(); i++){
            categories.add(categoryService.findById(recipes.get(i).getCategory_id()).get().getName());
            recipes.get(i).setCategory(categoryService.findByName(categories.get(i)));
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());
        return "my_recipes";
    }

    @GetMapping({"/edit_recipe"})
    public String editRecipePage(Long recipe_id, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("username", userDetails.getUsername());

        List<Recipe> recipes = recipeService.findByUser_id(userService.findByUsername(userDetails.getUsername()).getId());
        for (int i=0; i<recipes.size(); i++){
            if (recipes.get(i).getId().equals(recipe_id)){
                Recipe recipe = recipeService.findById(recipe_id).get();
                model.addAttribute("name", recipe.getName());
                model.addAttribute("description", recipe.getDescription());
                model.addAttribute("recipe_id", recipe_id);
                List<Category> categories = categoryService.findAll();
                model.addAttribute("categories", categories);
                return "edit_recipe";
            }
        }

        return "my_recipes";

    }

    @PostMapping({"/edit_recipe"})
    public String editRecipe(Recipe recipeForm, MultipartFile file, Long categ, Long recipe_id, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        Recipe recipe = recipeService.findById(recipe_id).get();

        recipe.setName(recipeForm.getName());

        recipe.setDescription(recipeForm.getDescription());

        recipe.setImage_path(recipeService.saveFile(file));

        recipe.setCategory_id(categ);

        recipe.setUser_id(userService.findByUsername(userDetails.getUsername()).getId());

        Date date = new Date();
        recipe.setCreation_date(date);

        if (recipeForm.getStatus()!=null && recipeForm.getStatus().equals("on")){
            recipe.setStatus(recipeForm.getStatus());
            recipe.setShort_link(recipeService.generateShortLink());
        }

        String category = categoryService.findById(categ).get().getName();

        recipeService.save(recipe);

        model.addAttribute("path",recipe.getImage_path());
        model.addAttribute("name",recipe.getName());
        model.addAttribute("date",recipe.getCreation_date());
        model.addAttribute("category",category);
        model.addAttribute("description",recipe.getDescription());

        model.addAttribute("username", userDetails.getUsername());
        Long id = recipeService.findByName(recipe.getName()).getId();
        return "redirect:/recipe/"+id;
    }

    @GetMapping({"/share_with_me"})
    public String shareWithMe(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Share> shares = shareService.findByName(userDetails.getUsername());
        List<Recipe> recipes = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < shares.size(); i++) {
            recipes.add(recipeService.findById(shares.get(i).getRecipe_id()).get());
            categories.add(categoryService.findById(recipes.get(i).getCategory_id()).get().getName());
            recipes.get(i).setCategory(categoryService.findByName(categories.get(i)));
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());
        return "share_with_me";
    }
}
