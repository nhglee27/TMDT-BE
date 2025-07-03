package com.example.Jewelry.dao;

import com.example.Jewelry.entity.AuctionProduct;
import com.example.Jewelry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionProductDAO extends JpaRepository<AuctionProduct, Integer> {

    Optional<AuctionProduct> findByProduct(Product product);
}
