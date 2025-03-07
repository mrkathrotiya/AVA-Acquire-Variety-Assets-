package com.server.AVA.Controllers;

import com.server.AVA.Config.JwtService;
import com.server.AVA.Models.DTOs.AuthDTOs.LoginDTO;
import com.server.AVA.Models.DTOs.AuthDTOs.LoginResponseDTO;
import com.server.AVA.Models.DTOs.AuthDTOs.RegisterUserDTO;
import com.server.AVA.Models.DTOs.UserDTOs.UpdateCredentials;
import com.server.AVA.Models.DTOs.UserDTOs.UserResponseDTO;
import com.server.AVA.Models.User;
import com.server.AVA.Services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
     private final AuthService authService;
     private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterUserDTO registerUserDTO) throws Exception {
        UserResponseDTO userResponseDTO = authService.registerUser(registerUserDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) throws Exception {
        User user = authService.authenticateUser(loginDTO);
        String token = jwtService.generateToken(user);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(token);
        loginResponseDTO.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponseDTO);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code ) throws Exception{
        return authService.googleCallback(code);
    }

    @GetMapping("/request-otp")
    public ResponseEntity<Map<String,String>> requestOTP(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(authService.sendOTP(email));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String email,
                                             @RequestParam String otp) throws Exception {
        return ResponseEntity.ok(authService.verifyUser(email, otp));
    }

}
