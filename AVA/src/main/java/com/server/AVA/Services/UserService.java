package com.server.AVA.Services;

import com.server.AVA.Models.DTOs.UserDTOs.UpdateCredentials;
import com.server.AVA.Models.DTOs.UserDTOs.UpdateUserDTO;
import com.server.AVA.Models.Property;
import com.server.AVA.Models.User;

import java.util.List;

public interface UserService {
    User getUser(String token) throws Exception;
    User findByEmail(String email) throws Exception;
    User updateUser(String token, UpdateUserDTO updateUserDTO) throws Exception;
    List<Property> getInterestedList(String token) throws Exception;
    User saveUser(User user);
    void removePropertyFromAllUserList(Long propertyId);
    String updateCredentials(String token, UpdateCredentials updateCredentials) throws Exception;
    String requestOTP(String token) throws Exception;
    String verifyOTP(String token, String OTP, UpdateCredentials updateCredentials) throws Exception;
}
