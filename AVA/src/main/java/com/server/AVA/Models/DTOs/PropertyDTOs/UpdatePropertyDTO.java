package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.DTOs.AddressDTO.AddressDTO;
import com.server.AVA.Models.enums.AreaType;
import com.server.AVA.Models.enums.PropertyType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePropertyDTO {
    private List<String> propertyImages;
    private List<String> paperImages;
    private Long price;
    private Long rent;
    private Boolean isForSell;
    private AddressDTO addressDTO;
    private AreaType areaType;
    private ShopDTO shopDTO;
    private LandDTO landDTO;
    private HouseDTO houseDTO;
    private FlatDTO flatDTO;
}
