package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.Category;
import com.nastya.cookbook.model.Recipe;
import com.nastya.cookbook.model.Share;
import com.nastya.cookbook.model.User;
import com.nastya.cookbook.service.CategoryService;
import com.nastya.cookbook.service.RecipeService;
import com.nastya.cookbook.service.ShareService;
import com.nastya.cookbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public String addRecipe(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("username", userDetails.getUsername());
        return "add_recipe";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipe(@PathVariable("id") Long id, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User user = userService.findByUsername(userDetails.getUsername());

        Recipe recipe = recipeService.findById(id).get();
        Category category = categoryService.findById(recipe.getCategory_id()).get();
        String recipe_username = userService.findById(recipe.getUser_id()).get().getUsername();

        model.addAttribute("recipe_username",recipe_username);
        model.addAttribute("category_id",recipe.getCategory_id());
        model.addAttribute("path",recipe.getImage_path());
        model.addAttribute("name",recipe.getName());
        model.addAttribute("date",recipe.getCreation_date());
        model.addAttribute("category",category.getName());
        model.addAttribute("description",recipe.getDescription());
        model.addAttribute("username", userDetails.getUsername());

        List<Share> shares = shareService.findByName(user.getUsername());
        List shares_id = new ArrayList<>();
        for (int i=0; i<shares.size(); i++){
            shares_id.add(shares.get(i).getRecipe_id());
            if (shares_id.get(i).equals(id)){
                return "recipe";
            }
        }

        if (recipe.getShort_link()!=null&&recipe.getStatus().equals("on")){
            if (recipe.getUser_id().equals(user.getId())){
                return "recipe";
            }
            else return "redirect:/";
        }

        return "recipe";
    }

    @GetMapping("/recipe/private/{shortlink}")
    public String getRecipeByShortLink(@PathVariable("shortlink") String shortlink, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        Recipe recipeForm = recipeService.findByShort_link(shortlink);
        Category category = categoryService.findById(recipeForm.getCategory_id()).get();
        String recipe_username = userService.findById(recipeForm.getUser_id()).get().getUsername();

        model.addAttribute("recipe_username",recipe_username);
        model.addAttribute("category_id",recipeForm.getCategory_id());
        model.addAttribute("path",recipeForm.getImage_path());
        model.addAttribute("name",recipeForm.getName());
        model.addAttribute("date",recipeForm.getCreation_date());
        model.addAttribute("category",category.getName());
        model.addAttribute("description",recipeForm.getDescription());
        model.addAttribute("username", userDetails.getUsername());
        return "recipe";
    }

    @PostMapping({"/add_recipe"})
    public String createRecipe(Recipe recipeForm, MultipartFile file, Long categ, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        if (categ==null){
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("message",2);
            return "add_recipe";
        }
        else {
            model.addAttribute("message",1);
        }

        recipeForm.setImage_path(recipeService.saveFile(file));

        recipeForm.setCategory_id(categ);

        recipeForm.setUser_id(userService.findByUsername(userDetails.getUsername()).getId());
        Date date = new Date();
        recipeForm.setCreation_date(date);

        if (recipeForm.getStatus()!=null && recipeForm.getStatus().equals("on")){
            recipeForm.setShort_link(recipeService.generateShortLink());
        }

        String category = categoryService.findById(categ).get().getName();

        recipeService.save(recipeForm);

        List<Recipe> recipes = recipeService.findAll();
        model.addAttribute("path",recipeForm.getImage_path());
        model.addAttribute("name",recipeForm.getName());
        model.addAttribute("date",recipeForm.getCreation_date());
        model.addAttribute("category",category);
        model.addAttribute("description",recipeForm.getDescription());

        model.addAttribute("username", userDetails.getUsername());
        Long id = recipes.get(recipes.size()-1).getId();
        return "redirect:/recipe/"+id;
    }


    @GetMapping("/category/{id}")
    public String getCategory(@PathVariable("id") Long id, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        Category category = categoryService.findById(id).get();
        List<User> users = new ArrayList<>();
        List<Recipe> recipes = recipeService.findByCategory_id(id);

        for (int i=0; i<recipes.size(); i++){
            users.add(i,userService.findById(recipes.get(i).getUser_id()).get());
            recipes.get(i).setUser(users.get(i));
            recipes.get(i).setCategory(category);
            if (recipes.get(i).getStatus()!=null && recipes.get(i).getShort_link()!=null){
                recipes.remove(i);
                i--;
            }
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());

        if (recipes.size()==0){
            model.addAttribute("message",2);
        }

        return "category";
    }


    @GetMapping({"/my_recipes"})
    public String myRecipes(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Recipe> recipes = recipeService.findByUser_id(userService.findByUsername(userDetails.getUsername()).getId());
        List<User> users = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (int i=0; i<recipes.size(); i++){
            users.add(i,userService.findById(recipes.get(i).getUser_id()).get());
            recipes.get(i).setUser(users.get(i));
            categories.add(categoryService.findById(recipes.get(i).getCategory_id()).get().getName());
            recipes.get(i).setCategory(categoryService.findByName(categories.get(i)));
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());
        if (recipes.size()==0){
            model.addAttribute("message",2);
        }
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

        List<User> users = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (int i=0; i<recipes.size(); i++){
            users.add(i,userService.findById(recipes.get(i).getUser_id()).get());
            recipes.get(i).setUser(users.get(i));
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

        if (userService.findByUsername(userDetails.getUsername()).getId().equals(recipe.getUser_id())){

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
        } else {
            model.addAttribute("message",1);
            return "edit_recipe";
        }
    }

    @GetMapping({"/share_with_me"})
    public String shareWithMe(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        List<User> users = new ArrayList<>();
        List<Share> shares = shareService.findByName(userDetails.getUsername());
        List<Recipe> recipes = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < shares.size(); i++) {
            recipes.add(recipeService.findById(shares.get(i).getRecipe_id()).get());

            users.add(i,userService.findById(recipes.get(i).getUser_id()).get());
            recipes.get(i).setUser(users.get(i));
            categories.add(categoryService.findById(recipes.get(i).getCategory_id()).get().getName());
            recipes.get(i).setCategory(categoryService.findByName(categories.get(i)));
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("username", userDetails.getUsername());
        if (recipes.size()==0){
            model.addAttribute("message",2);
        }
        return "share_with_me";
    }
}
