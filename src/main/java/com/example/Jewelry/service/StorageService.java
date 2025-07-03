package com.example.Jewelry.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    // Profile Image
    List<String> loadAll();

    String store(MultipartFile file);

    Resource load(String fileName);

    void delete(String fileName);

    // Course Videos
    List<String> loadAllProductImage();

    String storeProductImage(MultipartFile file);

    Resource loadProductImage(String fileName);

    void deleteProductImage(String fileName);

    // Category Image
    List<String> loadAllCategoryImage();

    String storeCategoryImage(MultipartFile file);

    Resource loadCategoryImage(String fileName);

    void deleteCategoryImage(String fileName);

}
