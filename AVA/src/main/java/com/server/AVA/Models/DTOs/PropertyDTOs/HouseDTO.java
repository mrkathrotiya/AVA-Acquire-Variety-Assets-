package com.server.AVA.Models.DTOs.PropertyDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HouseDTO {
    private Short floors;
    private Boolean isGarage;
    private Integer garageSize;
    private HomeDTO homeDTO;
}
