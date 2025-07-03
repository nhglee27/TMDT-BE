package com.example.Jewelry.dao;

import com.example.Jewelry.entity.Category;
import com.example.Jewelry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryAndStatusOrderByIdDesc(Category category, String status);

    List<Product> findByStatusAndNameContainingIgnoreCaseOrderByIdDesc(String status, String name);

    List<Product> findByStatusOrderByIdDesc(String status);

    List<Product> findByDeletedFalse();
    List<Product> findByDeletedTrue();

    @Query("SELECT p FROM Product p WHERE p.status = :status AND p.category.name = :categoryName")
    List<Product> getByCategoryNameAndStatus(@Param(("categoryName")) String categoryName, @Param(("status")) String status);

    @Query("SELECT p FROM Product p WHERE p.status = :status")
    List<Product> findAllByStatusOpenAuction(@Param("status") String status);

    @Query("SELECT p FROM Product p join AuctionProduct a ON p.id = a.product.id join AuctionRoom r ON r.currentAuction.id = a.id WHERE p.status = :status and (a.author.id = :userID OR r.collaborator.user.id = :userID)")
    List<Product> findAllMyProductAuction(@Param("status") String status, @Param("userID") int userID);
}
