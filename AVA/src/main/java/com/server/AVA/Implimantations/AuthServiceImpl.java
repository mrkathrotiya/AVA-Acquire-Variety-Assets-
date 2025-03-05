package com.server.AVA.Implimantations;

import com.server.AVA.Config.JwtService;
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
import com.server.AVA.Services.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.hibernate.action.internal.CollectionAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    private final JwtService jwtService;
    private final AddressHelper addressHelper;
    private final RestTemplate restTemplate;
    private final UserDetailServiceImpl userDetailService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
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

    @Override
    public ResponseEntity<?> googleCallback(String code) throws Exception {
        try {
            String tokenEndpoint = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code",code);
            params.add("client_id",clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri","https://developers.google.com/oauthplayground");
            params.add("grant_type","authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params,headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint,request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token="+idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK){
                Map userInfo = userInfoResponse.getBody();

                //Email came null --> Solve if you can..
                String email = (String) userInfo.get("email");
                String name = (String) userInfo.get("name");
                UserDetails userDetails = null;
                try {
                    userDetails = userDetailService.loadUserByUsername(email);
                }catch (Exception e){
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    Set<Role> roles = new HashSet<>();
                    roles.add(Role.BUYER);
                    user.setRoles(roles);
                    user.setInterestedList(new ArrayList<>());
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user = userRepository.save(user);
                    Buyer buyer = new Buyer();
                    buyer.setUser(user);
                    buyerRepository.save(buyer);
                    userDetails = userDetailService.loadUserByUsername(email);
                }
                String token = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(Collections.singletonMap("token",token));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
