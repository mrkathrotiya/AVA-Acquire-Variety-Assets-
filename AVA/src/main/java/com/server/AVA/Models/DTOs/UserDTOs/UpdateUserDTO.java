package com.server.AVA.Models.DTOs.UserDTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UpdateUserDTO {
    private String name;
    private Short age;
    private Date dob;
    private Boolean isBuyer;
    private Boolean isSeller;
}
