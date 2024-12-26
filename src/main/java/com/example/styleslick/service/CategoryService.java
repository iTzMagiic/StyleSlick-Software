package com.example.styleslick.service;

import com.example.styleslick.model.Category;
import com.example.styleslick.model.Database;

import java.util.List;

public class CategoryService {
    private static CategoryService categoryService;
    private Database database;


    private CategoryService() {}

    public static synchronized CategoryService getInstance() {
        if (categoryService == null) {
            categoryService = new CategoryService();
        }
        return categoryService;
    }


    public void setDatabase(Database database) {
        this.database = database;
    }


    public List<Category> getAllCategories() {
        return database.getAllCategories();
    }


    public void clearSession() {
        categoryService = null;
        database = null;
    }
}
