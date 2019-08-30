package com.nastya.cookbook.repository;

import com.nastya.cookbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fishn on 15.08.2019.
 */
//@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

//    @Modifying
//    @Query("update User u set u.username = ?1 where u.id = ?2")
//    void setFixedUsernameFor(String username, Long id);
//
//    @Modifying
//    @Query("update User u set u.email = ?1 where u.id = ?2")
//    void setFixedEmailFor(String email, Long id);
//
//    @Modifying
//    @Query("update User u set u.first_name = ?1 where u.id = ?2")
//    void setFixedFirstnameFor(String first_name, Long id);
//
//    @Modifying
//    @Query("update User u set u.last_name = ?1 where u.id = ?2")
//    void setFixedLastnameFor(String last_name, Long id);
}
