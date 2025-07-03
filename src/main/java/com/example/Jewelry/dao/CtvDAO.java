package com.example.Jewelry.dao;

import com.example.Jewelry.entity.CTV;
import com.example.Jewelry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtvDAO extends JpaRepository<CTV, Integer> {
    Optional<CTV> findByUser(User user);
    List<CTV> findByStatus(String status);
}
