package com.server.AVA.Implimantations;

import com.server.AVA.Helpers.AddressHelper;
import com.server.AVA.Models.*;
import com.server.AVA.Models.DTOs.AddressDTO.AddressDTO;
import com.server.AVA.Models.DTOs.AuthDTOs.LoginDTO;
import com.server.AVA.Models.DTOs.AuthDTOs.RegisterUserDTO;
import com.server.AVA.Models.DTOs.UserDTOs.UserResponseDTO;
import com.server.AVA.Models.enums.Role;
import com.server.AVA.Repos.AddressRepository;
import com.server.AVA.Repos.BuyerRepository;
import com.server.AVA.Repos.SellerRepository;
import com.server.AVA.Repos.UserRepository;
import com.server.AVA.Services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final AddressHelper addressHelper;

    @Override
    public UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws Exception {
        // Validate required fields
        Objects.requireNonNull(registerUserDTO.getName(), "Name cannot be null");
        Objects.requireNonNull(registerUserDTO.getEmail(), "Email cannot be null");
        Objects.requireNonNull(registerUserDTO.getPassword(), "Password cannot be null");
        Objects.requireNonNull(registerUserDTO.getRoles(), "Roles cannot be null");

        User user = new User();
        user.setName(registerUserDTO.getName());
        user.setAge(registerUserDTO.getAge());
        user.setDOB(registerUserDTO.getDOB());
        user.setEmail(registerUserDTO.getEmail());
        user.setPhone(registerUserDTO.getPhone());
        user.setRoles(new HashSet<>(registerUserDTO.getRoles()));
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setInterestedList(new ArrayList<>());

        Address address = addressHelper.mapAddressDTOToEntity(registerUserDTO.getAddressDTO());
        user.setAddress(address);
        addressRepository.save(address);

        userRepository.save(user);

        if (user.getRoles().contains(Role.SELLER)) {
            Seller seller = new Seller();
            seller.setIdentity(registerUserDTO.getIdentity());
            seller.setSoldList(new ArrayList<>());
            seller.setSellList(new ArrayList<>());
            seller.setUser(user);
            sellerRepository.save(seller);
        }
        if (user.getRoles().contains(Role.BUYER)) {
            Buyer buyer = new Buyer();
            buyer.setIdentity(registerUserDTO.getIdentity());
            buyer.setUser(user);
            buyerRepository.save(buyer);
        }

        return UserResponseDTO.builder()
                .name(user.getName())
                .identity(registerUserDTO.getIdentity())
                .email(user.getEmail())
                .DOB(user.getDOB())
                .address(user.getAddress())
                .roles(new ArrayList<>(user.getRoles()))
                .age(user.getAge())
                .password(user.getPassword())
                .isSeller(user.getRoles().contains(Role.SELLER))
                .isBuyer(user.getRoles().contains(Role.BUYER))
                .phone(user.getPhone())
                .build();
    }

    @Override
    public User authenticateUser(LoginDTO loginDTO) throws Exception{
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );
        return userRepository.findByEmail(loginDTO.getEmail()).orElseThrow();
    }
}
