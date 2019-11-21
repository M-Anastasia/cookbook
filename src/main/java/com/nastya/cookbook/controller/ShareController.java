package com.nastya.cookbook.controller;

import com.nastya.cookbook.model.Share;
import com.nastya.cookbook.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ShareController {

    @Autowired
    private ShareService shareService;

    @RequestMapping(value = "/share_recipe_by_username", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String shareByUsername(String name, String recipe_id, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        model.addAttribute("username", userDetails.getUsername());

        List<Share> shares = shareService.findByName(name);
        for (int i = 0; i < shares.size(); i++) {
            if (shares.get(i).getRecipe_id() == Long.valueOf(recipe_id)) {
                return "{\"status\":\"You have shared your recipe with user: " + name + "\"}";
            }
        }
        Share share = new Share(name, Long.valueOf(recipe_id));

        shareService.save(share);

        return "{\"status\":\"You have shared your recipe with user: " + name + "\"}";
    }

}
