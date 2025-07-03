package com.example.Jewelry.dao;

import com.example.Jewelry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByEmailId(String email);

    User findByEmailIdAndStatus(String email, String status);

    User findByRoleAndStatusIn(String role, List<String> status);

    List<User> findByRole(String role);

    User findByEmailIdAndRoleAndStatus(String emailId, String role, String status);

    List<User> findByRoleAndStatus(String role, String status);

    Optional<User> findById(Integer id);
    @Transactional
    @Modifying
    @Query("UPDATE User a SET a.status = 'Active' WHERE a.emailId = ?1")
    int activeUser(String email);

    @Query("SELECT u FROM User u WHERE u.oauth2_id = :oauth2Id")
    User findByOauth2Id(@Param("oauth2Id") String oauth2Id);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.username = :username")
    User findByUsernameAndStatus(@Param("username") String username, @Param("status") String status);

}

