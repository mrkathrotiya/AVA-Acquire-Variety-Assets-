package com.server.AVA.Models.DTOs.PropertyDTOs;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PropertyResponse {
    private PropertyDTO propertyDTO;
    private InsightsDTO insightsDTO;
    private ShopDTO shopDTO;
    private LandDTO landDTO;
    private FlatDTO flatDTO;
    private HouseDTO houseDTO;
}
