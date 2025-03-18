package com.server.AVA.Models.DTOs.PropertyDTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseDTO {
    private Short floors;
    private Boolean isGarage;
    private Integer garageSize;
    private HomeDTO homeDTO;
}
