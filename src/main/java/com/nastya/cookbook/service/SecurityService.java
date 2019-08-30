package com.nastya.cookbook.service;

/**
 * Created by fishn on 16.08.2019.
 */
public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
