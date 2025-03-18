package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.enums.LandType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {
    private Integer area;
    private Short floorNo;
    private Integer shopNo;
    private Boolean isComplex;
}
