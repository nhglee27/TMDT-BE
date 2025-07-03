package com.example.Jewelry.service;


import com.example.Jewelry.dto.request.RegisterCTVRequest;
import com.example.Jewelry.entity.CTV;
import com.example.Jewelry.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);

    User findByEmail(String email);

    String generateToken(User user);

    User updateUser(User user);

    User getUserByEmailAndStatus(String emailId, String status);

    User getUserByEmailid(String emailId);

    List<User> getUserByRole(String role);

    User getUserById(int userId);

    User getUserByEmailIdAndRoleAndStatus(String emailId, String role, String status);

    List<User> updateAllUser(List<User> users);

    List<User> getUserByRoleAndStatus(String role, String status);

    int activeUser(String email);

    List<User> getAllUser();

    boolean registerCTVUser(RegisterCTVRequest request);

    boolean updateCTVStatus(int id, boolean isConfirmed);
    Optional<User> verifyResetPasswordToken(String token);

    User getUserByUsernameAndStatus(String email, String value);

    CTV getCTVById(int userId);

    CTV getCTVByUserId(int ctvID);
}
