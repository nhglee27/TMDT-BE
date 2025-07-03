package com.example.Jewelry.dao;

import com.example.Jewelry.entity.Order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    Page<Order> findByUserEmailOrderByCreatedAtDesc(String userEmail, Pageable pageable);
    Optional<Order> findByIdAndUserEmail(Integer id, String userEmail);
    //được phép đánh giá sau khi đã mua hàng
    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.items oi " +
            "WHERE o.userEmail = :userEmail AND oi.productId = :productId AND o.status = :status")
    boolean existsOrderWithProductByUserEmailAndStatus(
            @Param("userEmail") String userEmail,
            @Param("productId") int productId,
            @Param("status") Order.OrderStatus status
    );
}
