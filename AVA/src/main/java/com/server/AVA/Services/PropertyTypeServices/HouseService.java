package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.DTOs.PropertyDTOs.HouseDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.House;
import com.server.AVA.Models.Land;

public interface HouseService {
    House getHouseByPropertyId(Long propertyId) throws Exception;
    void saveHouse(HouseDTO houseDTO, Long propertyId) throws Exception;
    void deleteHouse(Long propertyId) throws Exception;
}
