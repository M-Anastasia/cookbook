package com.nastya.cookbook.service;

import com.nastya.cookbook.model.Share;
import com.nastya.cookbook.repository.ShareRepository;

import java.util.List;

/**
 * Created by fishn on 04.09.2019.
 */
public interface ShareService {

    List<Share> findByName(String name);
    void save(Share share);
}
