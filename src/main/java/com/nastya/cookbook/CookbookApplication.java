package com.nastya.cookbook;

import com.nastya.cookbook.model.Category;
import com.nastya.cookbook.model.User;
import com.nastya.cookbook.repository.UserRepository;
import com.nastya.cookbook.service.CategoryService;
import com.nastya.cookbook.service.CategoryServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CookbookApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CookbookApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CookbookApplication.class, args);
	}

}
