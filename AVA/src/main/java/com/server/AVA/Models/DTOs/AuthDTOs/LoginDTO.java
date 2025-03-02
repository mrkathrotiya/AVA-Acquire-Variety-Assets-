package com.server.AVA.Models.DTOs.AuthDTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginDTO {
    private String email;
    private String password;
}
