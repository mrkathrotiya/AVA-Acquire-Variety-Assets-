package com.server.AVA.Models.DTOs.PropertyDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyResponse {
    private PropertyDTO propertyDTO;
    private InsightsDTO insightsDTO;
    private ShopDTO shopDTO;
    private LandDTO landDTO;
    private FlatDTO flatDTO;
    private HouseDTO houseDTO;
}
