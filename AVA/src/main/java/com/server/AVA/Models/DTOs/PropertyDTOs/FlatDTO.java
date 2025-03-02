package com.server.AVA.Models.DTOs.PropertyDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FlatDTO {
    private Short floorNo;
    private Boolean isElevator;
    private HomeDTO homeDTO;
}
