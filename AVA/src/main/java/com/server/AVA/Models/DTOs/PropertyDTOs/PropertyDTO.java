package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.DTOs.AddressDTO.AddressDTO;
import com.server.AVA.Models.enums.AreaType;
import com.server.AVA.Models.enums.PropertyType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PropertyDTO{
    private List<String> propertyImages = new ArrayList<>();
    private List<String> paperImages = new ArrayList<>();
    private PropertyType propertyType;
    private Long price;
    private Long rent;
    private Boolean isForSell;
    private AddressDTO addressDTO;
    private AreaType areaType;
}
