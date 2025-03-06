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
    public ResponseEntity<String> requestOTP(@RequestHeader("Authorization") String token) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = Objects.requireNonNull(token.substring(7));
        }
        String response = authService.requestOTP(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-and-update")
    public ResponseEntity<String> verifyAndUpdate(@RequestHeader("Authorization") String token,
                                                  @RequestParam String OTP,
                                                  @RequestBody UpdateCredentials updateCredentials) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = Objects.requireNonNull(token.substring(7));
        }
        String response = authService.verifyOTP(token,OTP,updateCredentials);
        return ResponseEntity.ok(response);
    }
}
