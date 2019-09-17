package com.nastya.cookbook.repository;

import com.nastya.cookbook.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Recipe findByName(String name);

    @Query("select r from Recipe r where r.user_id = ?1")
    List<Recipe> findByUser_id(Long user_id);

    @Query("select r from Recipe r where r.short_link = ?1")
    Recipe findByShort_link(String short_link);

    @Query("select r from Recipe r where r.category_id = ?1")
    List<Recipe> findByCategory_id(Long category_id);
}
