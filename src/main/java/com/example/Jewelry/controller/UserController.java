package com.example.Jewelry.controller;

import com.example.Jewelry.dto.UserDTO;
import com.example.Jewelry.dto.request.UserLoginRequest;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.dto.response.UserDTOResponse;
import com.example.Jewelry.Utility.Constant;
import com.example.Jewelry.dao.CtvDAO;
import com.example.Jewelry.dto.request.ChangePasswordRequestDTO;
import com.example.Jewelry.dto.request.RegisterCTVRequest;
import com.example.Jewelry.dto.request.RegisterUserRequest;
import com.example.Jewelry.dto.response.ImageUploadResponse;
import com.example.Jewelry.dto.response.UserLoginResponse;
import com.example.Jewelry.entity.CTV;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.resource.UserResource;
import com.example.Jewelry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    UserResource userResource;

    @Autowired
    UserService userService;

    @Autowired
    CtvDAO ctvDao;

    @PostMapping("/login")
    @Operation(summary = "Api to login any User")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        LOG.info("Received request for User Login " + userLoginRequest.toString());
        return userResource.login(userLoginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> register(@RequestBody RegisterUserRequest request){
        return userResource.registerUser(request);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<CommonApiResponse> confirm(@RequestParam("token") String token) {
        return userResource.confirmToken(token);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") int userId) {
        return userResource.getUserById(userId);
    }

    @GetMapping(path = "/resend-confirmation")
    public ResponseEntity<CommonApiResponse> resendConfirm(@RequestParam("token") String email) {
        return userResource.resendConfirmToken(email);
    }

    @GetMapping(value = "/{userImageName}", produces = "image/*")
    public void fetchTourImage(@PathVariable("userImageName") String userImageName, HttpServletResponse resp) {
        this.userResource.fetchUserImage(userImageName, resp);
    }

    @GetMapping(value = "/users")
    public  ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getAllUser();
        List<UserDTO> userDTOs = users.stream().map(UserDTO::toUserDtoEntity).toList();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping(value = "/info/{userID}")
    public ResponseEntity<UserDTOResponse> getUserByID(@PathVariable int userID) {
        UserDTOResponse response = new UserDTOResponse();
        User user = userService.getUserById(userID);
        if (user == null) {
            response.setSuccess(false);
            response.setResponseMessage("Error: No user founded with this id!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        UserDTO userDTO = UserDTO.toUserDtoEntity(user);
        response.setSuccess(true);
        response.setResponseMessage("Get user detail successfully!");
        response.setData(userDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/update/{userId}")
    public ResponseEntity<CommonApiResponse> updateUserProfile(@PathVariable("userId") int userId, @RequestBody UserDTO request) {
        return userResource.updateUserProfile(userId, request);
    }

    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity<ImageUploadResponse> uploadAvatar(
            @PathVariable int id,
            @RequestParam("avatar") MultipartFile file) {

        ImageUploadResponse response = new ImageUploadResponse();
        User user = userService.getUserById(id);

        if (file.isEmpty()) {
            response.setResponseMessage("File is empty.");
            response.setSuccess(false);
            response.setImageURL(user.getAvatar());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            userResource.updateUserAvatar(id, file);
            response.setResponseMessage("Avatar updated successfully");
            response.setSuccess(true);
            response.setImageURL(user.getAvatar());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage("Error saving avatar: " + e.getMessage());
            response.setSuccess(false);
            response.setImageURL("");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/register-ctv")
    public ResponseEntity<CommonApiResponse> registerCTV(@RequestBody RegisterCTVRequest request) {
        return userResource.registerCTV(request);
    }
    
    @PostMapping("/{id}/confirm-CTV")
    public ResponseEntity<CommonApiResponse> confirmCTV(@PathVariable int id, @RequestParam boolean isConfirmed) {
        boolean result = userService.updateCTVStatus(id, isConfirmed);
        CommonApiResponse response = new CommonApiResponse();
        if (result) {
            response.setSuccess(true);
            response.setResponseMessage(isConfirmed ? "User đã được cấp quyền CTV." : "Yêu cầu đã bị từ chối.");
        } else {
            response.setSuccess(false);
            response.setResponseMessage("Không thể cập nhật trạng thái.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ctv-pending")
    @Operation(summary = "List pending CTV registrations")
    public ResponseEntity<List<RegisterCTVRequest>> getPendingCTV() {
        List<CTV> pendingProfiles = ctvDao.findByStatus(Constant.CtvStatus.PENDING.value());

        List<RegisterCTVRequest> result = pendingProfiles.stream().map(ctv -> {
            User user = ctv.getUser();
            return new RegisterCTVRequest(
                    user.getId(),
                    user.getEmailId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNo(),
                    ctv.getLocation(),
                    ctv.getExperienceAndSkills(),
                    ctv.getSampleWorkLink(),
                    ctv.getReason()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    // Change Password
    @PutMapping("/change-password")
    @Operation(summary = "Api to change password")
    public ResponseEntity<CommonApiResponse> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        return userResource.changePassword(request);
    }


    // Forget password.
    @GetMapping("/forget-password")
    @Operation(summary = "Api to login any User")
    public ResponseEntity<CommonApiResponse> forgetPassword(@RequestParam String email) {
        return userResource.forgetPassword(email);
    }

    @GetMapping("/verify-reset-token")
    public ResponseEntity<?> verifyResetToken(@RequestParam("token") String token) {
        Optional<User> userOpt = userService.verifyResetPasswordToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token không hợp lệ hoặc đã hết hạn.");
        }

        User user = userOpt.get();

        return ResponseEntity.ok(Map.of(
                "email", user.getEmailId(),
                "username", user.getUsername()
        ));
    }

    @PutMapping("/reset-password")
    @Operation(summary = "Api to reset password")
    public ResponseEntity<CommonApiResponse> resetPassword(@RequestBody ChangePasswordRequestDTO request) {
        return userResource.resetPassword(request);
    }

}
