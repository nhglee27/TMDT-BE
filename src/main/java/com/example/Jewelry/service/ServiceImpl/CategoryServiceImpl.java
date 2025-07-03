package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.CategoryDAO;
import com.example.Jewelry.entity.Category;
import com.example.Jewelry.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public Category addCategory(Category category) {
        return this.categoryDAO.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return this.categoryDAO.save(category);
    }

    @Override
    public Category getCategoryById(int categoryId) {

        Optional<Category> optionalCategory = this.categoryDAO.findById(categoryId);

        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        } else {
            return null;
        }

    }
    @Override
    public List<Category> getCategoriesByStatusIn(List<String> status) {
        return this.categoryDAO.findByStatusIn(status);
    }

    @Override
    public boolean existsByName(String name) {
        return this.categoryDAO.existsByName(name);
    }

    @Override
    public List<Category> getAllCategoriesDeletedFalse() {
        return this.categoryDAO.findByDeletedFalse();
    }
    @Override
    public List<Category> getAllCategoriesDeletedTrue() {
        return this.categoryDAO.findByDeletedTrue();
    }
}
