package com.example.Jewelry.service;

import com.example.Jewelry.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category);
    Category getCategoryById(int category);
    List<Category> getCategoriesByStatusIn(List<String> status);
    boolean existsByName(String name);
    List<Category> getAllCategoriesDeletedFalse();
    List<Category> getAllCategoriesDeletedTrue();
}
