package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.enums.LandType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LandDTO {
    private Integer area;
    private LandType landType;
}
