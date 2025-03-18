package com.server.AVA.Models.DTOs.PropertyDTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlatDTO {
    private Short floorNo;
    private Boolean isElevator;
    private HomeDTO homeDTO;
}
