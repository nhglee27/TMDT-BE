package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.Utility.Constant;
import com.example.Jewelry.dao.CtvDAO;
import com.example.Jewelry.dao.UserDAO;
import com.example.Jewelry.dto.request.RegisterCTVRequest;
import com.example.Jewelry.entity.CTV;
import com.example.Jewelry.entity.ConfirmationToken;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDao;

    @Autowired
    private CtvDAO ctvDao;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public User addUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmailId(email);
    }

    @Override
    public String generateToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        String stringToken = UUID.randomUUID().toString();
        confirmationToken.setToken(stringToken);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        confirmationTokenService.save(confirmationToken);
        return stringToken;
    }

    @Override
    public User updateUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User getUserByEmailAndStatus(String emailId, String status) {
        return userDao.findByEmailIdAndStatus(emailId, status);
    }

    @Override
    public User getUserByEmailid(String emailId) {
        return userDao.findByEmailId(emailId);
    }

    @Override
    public List<User> getUserByRole(String role) {
        return userDao.findByRole(role);
    }

    @Override
    public User getUserById(int userId) {

        Optional<User> optionalUser = this.userDao.findById(userId);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return null;
        }

    }

    @Override
    public User getUserByEmailIdAndRoleAndStatus(String emailId, String role, String status) {
        return this.userDao.findByEmailIdAndRoleAndStatus(emailId, role, status);
    }

    @Override
    public List<User> updateAllUser(List<User> users) {
        return this.userDao.saveAll(users);
    }

    @Override
    public List<User> getUserByRoleAndStatus(String role, String status) {
        return this.userDao.findByRoleAndStatus(role, status);
    }

    @Override
    public int activeUser(String email) {
        return userDao.activeUser(email);
    }


    @Override
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    public boolean registerCTVUser(RegisterCTVRequest request) {
        User user = userDao.findByEmailId(request.getEmail());
        if (user == null || user.getRole().equals(Constant.UserRole.ROLE_CTV.value())) {
            return false; // user chưa tồn tại hoặc đã là CTV
        }

        // Cập nhật thông tin người dùng cơ bản
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNo(request.getPhoneNo());
        user.setStatus(Constant.ActiveStatus.DEACTIVATED.value());
        userDao.save(user);

        CTV profile = new CTV();
        profile.setUser(user);
        profile.setPhoneNo(request.getPhoneNo());
        profile.setLocation(request.getLocation());
        profile.setExperienceAndSkills(request.getExperienceAndSkills());
        profile.setSampleWorkLink(request.getSampleWorkLink());
        profile.setReason(request.getReason());
        profile.setCreatedAt(LocalDateTime.now());
        profile.setStatus(Constant.CtvStatus.PENDING.value());
        ctvDao.save(profile);
        return true;
    }

    public boolean updateCTVStatus(int userId, boolean isConfirmed) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<CTV> ctvOptional = ctvDao.findByUser(user);
            if (ctvOptional.isPresent()) {
                CTV ctv = ctvOptional.get();
                if (isConfirmed) {
                    ctv.setStatus(Constant.CtvStatus.APPROVED.value());
                    user.setStatus(Constant.ActiveStatus.ACTIVE.value());
                    user.setRole(Constant.UserRole.ROLE_CTV.value());

                } else {
                    ctv.setStatus(Constant.CtvStatus.REJECTED.value());
//                    ctvDao.delete(ctv);
                    user.setStatus(Constant.ActiveStatus.DEACTIVATED.value());
                }

                ctvDao.save(ctv);
                userDao.save(user);
                return true;
            }
        }
        return false;
    }



    @Override
    public Optional<User> verifyResetPasswordToken(String token) {
        Optional<ConfirmationToken> optionalToken = confirmationTokenService.getToken(token);

        if (optionalToken.isEmpty()) {
            return Optional.empty();
        }

        ConfirmationToken confirmationToken = optionalToken.get();

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        return Optional.of(confirmationToken.getUser());
    }

    @Override
    public User getUserByUsernameAndStatus(String username, String status) {
        return userDao.findByUsernameAndStatus(username, status);
    }

    @Override
    public CTV getCTVById(int userId) {
        Optional<CTV> optionalUser = this.ctvDao.findById(userId);
        if (optionalUser.isPresent()) {
            CTV ctv = optionalUser.get();
            if (ctv.getStatus().equals(Constant.CtvStatus.APPROVED.value()))
                return ctv;
            return null;
        } else {
            return null;
        }

    }

    @Override
    public CTV getCTVByUserId(int userID) {
        Optional<User> userOptional = userDao.findById(userID);
        if (userOptional.isPresent()) {
            Optional<CTV> ctvOptional = ctvDao.findByUser(userOptional.get());
            if (!ctvOptional.isPresent())
                return null;

            CTV ctv = ctvOptional.get();
            if (ctv.getStatus().equals(Constant.CtvStatus.APPROVED.value()))
                return ctv;
            return null;
        } else {
            return null;
        }

    }

}
