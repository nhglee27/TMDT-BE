package com.example.Jewelry.dao;

import com.example.Jewelry.entity.WishlistItem;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemDAO extends JpaRepository<WishlistItem, Integer> {
    List<WishlistItem> findByUserAndDeletedFalse(User user);
    Optional<WishlistItem> findByUserAndProductAndDeletedFalse(User user, Product product);
}
