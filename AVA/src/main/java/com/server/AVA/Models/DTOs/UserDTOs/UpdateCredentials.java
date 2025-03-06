package com.server.AVA.Models.DTOs.UserDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCredentials {
    private String email;
    private String phone;
    private String password;
}
