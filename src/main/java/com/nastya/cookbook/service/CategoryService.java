package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category findByName(String name);

    void save(Category category);

    List<Category> findAll();

    Optional<Category> findById(Long id);
}
