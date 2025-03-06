package com.server.AVA.Implimantations;

import com.server.AVA.Config.JwtService;
import com.server.AVA.Models.DTOs.UserDTOs.UpdateCredentials;
import com.server.AVA.Models.DTOs.UserDTOs.UpdateUserDTO;
import com.server.AVA.Models.Property;
import com.server.AVA.Models.Seller;
import com.server.AVA.Models.User;
import com.server.AVA.Models.enums.Role;
import com.server.AVA.Repos.BuyerRepository;
import com.server.AVA.Repos.SellerRepository;
import com.server.AVA.Repos.UserRepository;
import com.server.AVA.Services.MailService;
import com.server.AVA.Services.PropertyService;
import com.server.AVA.Services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SellerRepository sellerRepository;
    private final MailService mailService;

    @Override
    public User getUser(String token) throws Exception{
        Objects.requireNonNull(token, "Token cannot be null!");

        String email = jwtService.extractUsername(token);
        return findByEmail(email);
    }

    @Override
    public User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public User updateUser(String token, UpdateUserDTO updateUserDTO) throws Exception {
        User existingUser = userRepository.findByEmail(jwtService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updateUserDTO.getName() != null) existingUser.setName(updateUserDTO.getName());
        if (updateUserDTO.getAge() != null) existingUser.setAge(updateUserDTO.getAge());
        if (updateUserDTO.getDob() != null) existingUser.setDOB(updateUserDTO.getDob());
        if (Boolean.TRUE.equals(updateUserDTO.getIsBuyer())) existingUser.getRoles().add(Role.BUYER);
        if (Boolean.TRUE.equals(updateUserDTO.getIsSeller())) {
            existingUser.getRoles().add(Role.SELLER);
            Seller seller = new Seller();
            seller.setUser(existingUser);
            seller.setSoldList(new ArrayList<>());
            seller.setSellList(new ArrayList<>());
            sellerRepository.save(seller);
        }
        if (Boolean.FALSE.equals(updateUserDTO.getIsSeller())) {
            existingUser.getRoles().remove(Role.SELLER);
            removeSeller(existingUser.getId());
        }

        existingUser = userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    @Transactional
    public List<Property> getInterestedList(String token) throws Exception {
        User user = getUser(token);
        return user.getInterestedList() != null ? user.getInterestedList() : new ArrayList<>();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if (user != null) return userRepository.save(user);
        return null;
    }

    private void removeSeller(Long userId) throws Exception {
        Optional<Seller> seller = sellerRepository.getSellerByUserId(userId);
        seller.ifPresent(sellerRepository::delete);
    }

    @Override
    public void removePropertyFromAllUserList(Long propertyId){
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.getInterestedList().removeIf(p -> p.getId().equals(propertyId));
            userRepository.save(user);
        }
    }

    @Override
    public String updateCredentials(String token, UpdateCredentials updateCredentials) throws Exception {
        //Email should not be null un update credential object
        User user = getUser(token);
        Optional.ofNullable(updateCredentials.getEmail()).ifPresent(user::setEmail);
        user = saveUser(user);

        return jwtService.generateToken(user);
    }
}
