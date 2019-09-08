package com.nastya.cookbook.repository;

import com.nastya.cookbook.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by fishn on 04.09.2019.
 */
public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query("select s from Share s where s.name = ?1")
    List<Share> findByName(String name);
}
