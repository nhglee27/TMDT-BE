package com.example.Jewelry.service;

import com.example.Jewelry.dto.ProductDTO;
import com.example.Jewelry.entity.Category;
import com.example.Jewelry.entity.Product;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    Product add(Product product);

    Product update(Product product);

    List<Product> updateAll(List<Product> products);

    Product getById(int id);

    List<Product> getAll();

    List<Product> getByStatus(String status);

    List<Product> getByCategoryAndStatus(Category category, String status);

    List<Product> getByNameAndStatus(String name, String status);
    List<Product> fetchAllProductDeleteTrue();

    List<Product> fetchAllProductDeleteFalse();

    void deleteProduct(int productId);

    List<ProductDTO> getActiveProductListForShop();

    ProductDTO getProductDetailsForUser(int id);

    List<Product> getByCategoryNameAndStatus(String categoryName, String status);

    List<Product> fetchAllProductOpenAuction(String status);


}
