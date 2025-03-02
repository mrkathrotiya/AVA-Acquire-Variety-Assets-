package com.server.AVA.Services;

import com.server.AVA.Models.DTOs.AuthDTOs.LoginDTO;
import com.server.AVA.Models.DTOs.AuthDTOs.RegisterUserDTO;
import com.server.AVA.Models.DTOs.UserDTOs.UserResponseDTO;
import com.server.AVA.Models.User;

public interface AuthService {
    UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws Exception;
    User authenticateUser(LoginDTO loginDTO) throws Exception;
}
