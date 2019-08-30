package com.nastya.cookbook.repository;

import com.nastya.cookbook.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fishn on 21.08.2019.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
