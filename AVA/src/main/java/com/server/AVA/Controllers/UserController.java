package com.server.AVA.Controllers;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.server.AVA.Config.JwtService;
import com.server.AVA.Models.DTOs.UserDTOs.UpdateUserDTO;
import com.server.AVA.Models.Property;
import com.server.AVA.Models.User;
import com.server.AVA.Repos.UserRepository;
import com.server.AVA.Services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token) throws Exception{
        Objects.requireNonNull(token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User user = userService.getUser(token);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @RequestBody UpdateUserDTO updateUserDTO) throws Exception {
        Objects.requireNonNull(token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User existingUser = userService.updateUser(token,updateUserDTO);
        return ResponseEntity.ok(existingUser);
    }

    @GetMapping("/profile/get-Interested")
    public ResponseEntity<List<Property>> getInterestedPropertyList(@RequestHeader("Authorization") String token) throws Exception {
        Objects.requireNonNull(token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return ResponseEntity.ok(userService.getInterestedList(token));
    }

}
