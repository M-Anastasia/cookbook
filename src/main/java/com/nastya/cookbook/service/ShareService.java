package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Share;

import java.util.List;

public interface ShareService {

    List<Share> findByName(String name);

    void save(Share share);

    List<Share> findByRecipe_id(Long recipe_id);

    void delete(Share share);
}
