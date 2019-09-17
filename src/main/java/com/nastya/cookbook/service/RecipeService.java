package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Recipe findByName(String name);

    void save(Recipe category);

    String saveFile(MultipartFile file);

    String generateShortLink();

    Optional<Recipe> findById(Long id);

    List<Recipe> findAll();

    List<Recipe> findByUser_id(Long user_id);

    void delete(Recipe recipe);

    Recipe findByShort_link(String short_link);

    List<Recipe> findByCategory_id(Long category_id);

    Page<Recipe> findPaginated(Pageable pageable, List<Recipe> recipes);
}
