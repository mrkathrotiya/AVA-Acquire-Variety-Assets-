package com.server.AVA.Services;

import com.server.AVA.Models.DTOs.AuthDTOs.LoginDTO;
import com.server.AVA.Models.DTOs.AuthDTOs.RegisterUserDTO;
import com.server.AVA.Models.DTOs.UserDTOs.UpdateCredentials;
import com.server.AVA.Models.DTOs.UserDTOs.UserResponseDTO;
import com.server.AVA.Models.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws Exception;
    User authenticateUser(LoginDTO loginDTO) throws Exception;
    ResponseEntity<?> googleCallback(String code) throws Exception;
    Map<String,String> sendOTP(String email) throws Exception;
    String verifyUser(String email, String otp) throws Exception;
}
