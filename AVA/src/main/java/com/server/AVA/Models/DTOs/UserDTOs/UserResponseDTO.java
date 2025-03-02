package com.server.AVA.Models.DTOs.UserDTOs;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.enums.Role;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserResponseDTO {
    private String name;
    private Short age;
    private String email;
    private String phone;
    private String password;
    private List<Role> roles;
    private Date DOB;
    private Address address;
    private String identity;

    private Boolean isSeller;
    private Boolean isBuyer;

}
