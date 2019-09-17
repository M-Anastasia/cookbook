package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Recipe;
import com.nastya.cookbook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by fishn on 21.08.2019.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public Recipe findByName(String name) {
        return recipeRepository.findByName(name);
    }

    @Override
    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public String saveFile(MultipartFile file) {
//        String resource = "src\\main\\resources\\static\\images\\recipes\\";
        String path = System.getenv("RECIPES_IMAGES");
        String resource = "uploads\\";
        InputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        String fn1 = "";
        try {
            fileInputStream = file.getInputStream();
            File f = File.createTempFile("recipe",".jpg",new File(path));
            fn1 = f.getName();
            fileOutputStream = new FileOutputStream(f);
            int element;
            while ((element = fileInputStream.read()) != -1){
                fileOutputStream.write(element);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fn1;
    }

    @Override
    public String generateShortLink() {
        String a = "0123456789ABCDEF";
        char[] b = a.toCharArray();
        String sl = "";

        for (int i=0; i<4; i++){
            int tmp = (int)(Math.random()*16);
            sl += b[tmp];
        }

        return sl;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public List<Recipe> findByUser_id(Long user_id) {
        return recipeRepository.findByUser_id(user_id);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public Recipe findByShort_link(String short_link) {
        return recipeRepository.findByShort_link(short_link);
    }

    public static void main(String[] argv){
        File file = new File("C:\\Users\\fishn\\Desktop\\donuts-4.jpg");
        String resource = "uploads\\";
        InputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        String fn1 = "";
        try {
            fileInputStream = new FileInputStream(file);
            File f = File.createTempFile("recipe",".jpg",new File(resource));
            fn1 = f.getName();
            fileOutputStream = new FileOutputStream(f);
            int element;
            while ((element = fileInputStream.read()) != -1){
                fileOutputStream.write(element);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Recipe> findByCategory_id(Long category_id) {
        return recipeRepository.findByCategory_id(category_id);
    }
}
