package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.enums.HomeAreaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SizeDTO {
    private HomeAreaType homeAreaType;
    private Integer size;
}
