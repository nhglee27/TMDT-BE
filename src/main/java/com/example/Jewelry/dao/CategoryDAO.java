package com.example.Jewelry.dao;

import com.example.Jewelry.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDAO extends JpaRepository<Category, Integer> {

    List<Category> findByStatusIn(List<String> status);
    boolean existsByName(String name);
    Long countByStatusIn(List<String> status);
    List<Category> findByDeletedFalse();
    List<Category> findByDeletedTrue();

}
