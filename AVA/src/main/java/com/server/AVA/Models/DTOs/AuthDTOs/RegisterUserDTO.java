package com.server.AVA.Models.DTOs.AuthDTOs;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.DTOs.AddressDTO.AddressDTO;
import com.server.AVA.Models.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RegisterUserDTO {
    private String name;
    private short age;
    private String email;
    private String phone;
    private String password;
    private List<Role> roles;
    private Date DOB;
    private AddressDTO addressDTO;
    private String identity;
}
