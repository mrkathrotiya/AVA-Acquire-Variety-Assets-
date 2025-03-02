package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.enums.PropertyType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreatePropertyDTO {
    private PropertyDTO propertyDTO;
    private ShopDTO shopDTO;
    private LandDTO landDTO;
    private HouseDTO houseDTO;
    private FlatDTO flatDTO;
}
