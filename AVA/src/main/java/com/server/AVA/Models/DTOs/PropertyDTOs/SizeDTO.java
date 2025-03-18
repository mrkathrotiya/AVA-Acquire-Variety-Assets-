package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.enums.HomeAreaType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeDTO {
    private HomeAreaType homeAreaType;
    private Integer size;
}
