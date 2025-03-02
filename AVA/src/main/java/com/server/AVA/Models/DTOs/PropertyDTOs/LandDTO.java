package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.enums.LandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LandDTO {
    private Integer area;
    private LandType landType;
}
